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
    idea apply true
    kotlin("jvm") version "1.3.61"
    id("org.jetbrains.intellij") version "0.5.0"
}
apply {
    plugin("java")
    plugin("kotlin")
    plugin("org.jetbrains.intellij")
}

println("Version: $version")
tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
        kotlinOptions.freeCompilerArgs += "-progressive"
    }
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
            if (readmeFile().readText() != generatedReadmeContent(readmeFile())) {
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
    patchPluginXml {
        changeNotes(project.property("changeNotes").toString().replace("\n", "<br>\n"))
    }
    named<Zip>("buildPlugin") {
        dependsOn("test")
        archiveFileName.set("${intellij.pluginName}.jar")
    }
    named("test"){
        dependsOn("checkReadme")
    }
    named("buildPlugin") {
        dependsOn("copyInspections")
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
            "java",
            "java-i18n",
            "PsiViewer:3.28.93",
            "properties"
    )
}
dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
}
fun prop(name: String): String {
    return extra.properties[name] as? String
            ?: error("Property `$name` is not defined in gradle.properties")
}

fun safeProp(name: String, fallback: String): String {
    return extra.properties[name] as? String
            ?: fallback
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
                "#### ${it.uid()}\n$description\n"
            }
            .joinToString("")
    return content
}

fun readmeFile() = File("README.md")
