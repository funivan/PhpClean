import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    mavenCentral()
    maven {
        url = uri("http://dl.bintray.com/jetbrains/intellij-plugin-service")
    }
}
buildscript {
    repositories {
        dependencies {
            classpath(kotlin("gradle-plugin", version = "1.3.21"))
        }
    }
}
plugins {
    id("org.jetbrains.intellij") version "0.4.9"
    id("org.jetbrains.kotlin.jvm") version "1.3.41"
    idea
}
apply {
    plugin("java")
    plugin("kotlin")
    plugin("org.jetbrains.intellij")
}
if (file("local.properties").exists()) {
    apply(from = "local.properties")
}
println("Version: $version")
val fileName = "$name.jar"
tasks {
    register("copyInspections") {
        doLast {
            blocks().forEach {
                write(
                        File("src/main/resources/inspectionDescriptions/" + it.file().name),
                        it.full()
                )
            }
        }
    }
    register("checkReadme") {
        doLast {
            if (readmeFile().readText()!= generatedReadmeContent(readmeFile())) {
                throw GradleException("Readme is not up to date")
            }
        }
    }
    register("updateReadme") {
        doLast {
            val readme = readmeFile()
            if (write(readme, generatedReadmeContent(readme))) {
                println("Readme updated")
            }
        }
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    patchPluginXml {
        changeNotes(project.property("changeNotes").toString().replace("\n", "<br>\n"))
    }
    named("buildPlugin") {
        dependsOn("copyInspections")
        dependsOn("patchRepositoryXml")
    }
}
intellij {
    version = prop("ideaVersion")
    sandboxDirectory = project.rootDir.canonicalPath + "/build/idea-sandbox"
    downloadSources = false
    updateSinceUntilBuild = false
    pluginName = name
    setPlugins(
            "com.jetbrains.php:${prop("phpPluginVersion")}",
            "CSS",
            "java-i18n",
            "PsiViewer:3.28.93",
            "properties"
    )
}
tasks.jar {
    archiveName = fileName
}

tasks.register<Copy>("patchRepositoryXml") {
    doFirst {
        delete("${buildDir}/libs/*.xml")
    }
    from("src/ci/PhpClean-nightly.xml")
    into("$buildDir/libs")
    expand(hashMapOf(
            "fileSize" to "18000",
            "version" to project.property("version").toString(),
            "pluginName" to name,
            "buildDate" to System.currentTimeMillis(),
            "fileName" to fileName,
            "group" to project.property("group")
    ))
}

tasks.register<Exec>("deployNightly") {
    commandLine = listOf(
            "curl", "-s",
            "-F", "file[]=@build/libs/${fileName}",
            "-F", "file[]=@build/libs/PhpClean-nightly.xml",
            safeProp("ci_deploy_uri", safeEnv("DEPLOY_URI", ""))
    )
}

dependencies {
    implementation(kotlin("stdlib"))
}
fun prop(name: String): String {
    return extra.properties[name] as? String
            ?: error("Property `$name` is not defined in gradle.properties")
}

fun safeProp(name: String, fallback: String): String {
    return extra.properties[name] as? String
            ?: fallback
}

fun safeEnv(name: String, fallback: String): String {
    return System.getenv(name) ?: fallback
}

fun write(file: File, content: String): Boolean {
    var result = false
    if (!file.exists()) {
        file.createNewFile()
    }
    if (file.readText() != content) {
        result = true
        file.writeText(content)
    }
    return result
}

class Block(private val file: File) {
    fun file() = file
    fun uid() = file.name.replace("Inspection.html", "")
    fun full() = file.readText()
    fun short() = full()
            .replace(Regex("<!-- main -->(.*)", RegexOption.DOT_MATCHES_ALL), "")
            .trim()
}

fun blocks() = File("src/main/kotlin/com/funivan/idea/phpClean/inspections")
        .walkTopDown()
        .filter { it.name.contains("Inspection.kt") }
        .map { Block(File(it.path.replace(".kt", ".html"))) }

fun generatedReadmeContent(readme: File): String {
    var content = readme.readText()
    content = content.replace(
            Regex("(<!-- inspections -->)(.+)", RegexOption.DOT_MATCHES_ALL),
            "$1"
    )
    content = content + "\n" + blocks().sortedBy { it.uid() }
            .map {
                val description = it.short().replace("<pre>", "```php").replace("</pre>", "```")
                "#### ${it.uid()}\n${description}\n"
            }
            .joinToString("")
    return content
}

fun readmeFile() = File("README.md")