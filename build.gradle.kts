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
    id("org.jetbrains.intellij") version "0.3.5"
    id("org.jetbrains.kotlin.jvm") version "1.3.21"
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
tasks {
    patchPluginXml {
        changeNotes(project.property("changeNotes").toString().replace("\n", "<br>\n"))
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
    archiveName = "$name.jar"
}

tasks.register<Copy>("patchRepositoryXml") {
    from("src/ci/PhpClean-nightly.xml")
    into("$buildDir/libs")
    expand(hashMapOf(
            "pluginName" to project.property("pluginName"),
            "fileSize" to "18000",
            "version" to project.property("version").toString(),
            "pluginName" to project.property("pluginName"),
            "buildDate" to System.currentTimeMillis(),
            "fileName" to "$name.jar",
            "group" to project.property("group")
    ))
}

tasks.register<Exec>("deployNightly") {
    commandLine = listOf(
            "curl", "-s", "-F",
            "file[]=@build/libs/PhpClean.jar",
            "-F",
            "file[]=@build/libs/PhpClean-nightly.xml",
            safeProp("ci_deploy_uri", safeEnv("DEPLOY_URI", ""))
    )
}

tasks.register("generateDocs") {
    val changed = mutableListOf<Boolean>()
    val blocks = mutableListOf<Block>()
    val inspectionDirectory = "src/main/resources/inspectionDescriptions"
    val directory = "src/main/kotlin/com/funivan/idea/phpClean/inspections"
    File(directory)
            .walkTopDown()
            .filter { it.name.contains("Inspection.kt") }
            .map { Block(File(it.path.replace(".kt", ".html"))) }
            .forEach {
                blocks.add(it)
                val file = File(inspectionDirectory + "/" + it.file().name)
                changed.add(
                        write(file, it.file().readText())
                )
            }
    val readme = File("README.md")
    var content = readme.readText()
    content = content.replace(
            Regex("(<!-- inspections -->)(.+)", RegexOption.DOT_MATCHES_ALL),
            "$1"
    )
    content = content + "\n" + blocks.sortedBy { it.uid() }
            .map { "#### ${it.uid()} \n${it.short()}\n" }
            .joinToString("")
    changed.add(write(readme, content))
    println("Changed files : " + changed.filter { it == true }.size)
}

dependencies {
    implementation(kotlin("stdlib"))
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
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
    fun short() = file.readText()
            .replace(Regex("<!-- main -->(.*)", RegexOption.DOT_MATCHES_ALL), "")
            .replace("<code>", "```php")
            .replace("</code>", "```").trim()
}

