
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
    id("org.jetbrains.intellij") version "1.10.0"
    id("org.jetbrains.changelog") version "1.3.1"
    // ktlint linter - read more: https://github.com/JLLeitschuh/ktlint-gradle
    // id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
}
fun properties(key: String) = project.findProperty(key).toString()

// Import variables from gradle.properties file
val pluginGroup: String by project
// `pluginName_` variable ends with `_` because of the collision with Kotlin magic getter in the `intellij` closure.
// Read more about the issue: https://github.com/JetBrains/intellij-platform-plugin-template/issues/29
val pluginName_: String by project
val pluginVersion: String by project
val pluginSinceBuild: String by project
val pluginVerifierIdeVersions: String by project
val intellijPublishChannel: String by project
val intellijPublishToken: String by project

val platformType: String by project
val platformVersion: String by project
val platformPlugins: String by project
val platformDownloadSources: String by project

group = pluginGroup
version = pluginVersion
println("version: $version")
// Configure project's dependencies
repositories {
    mavenCentral()
    jcenter()
}

// Configure gradle-intellij-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set(pluginName_)
    version.set(platformVersion)
    type.set(platformType)
    downloadSources.set(platformDownloadSources.toBoolean())
    updateSinceUntilBuild.set(false)
    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}
dependencies {
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

// Read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    version.set(pluginVersion)
    headerParserRegex.set("""(\d+\.\d+\.\d+)""".toRegex())
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    publishPlugin {
        token.set(intellijPublishToken)
        channels.set(listOf(intellijPublishChannel, "default").filter(String::isNotEmpty).take(1))
    }
    patchPluginXml {
        version.set(pluginVersion)
        sinceBuild.set(pluginSinceBuild)
        changeNotes.set(
            changelog.getLatest().toHTML()
        )
    }

    runPluginVerifier {
        ideVersions.set(
            properties("pluginVerifierIdeVersions")
                .split(',').map(String::trim).filter(String::isNotEmpty)
        )
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

    named("test"){
        dependsOn("checkReadme")
    }
    named("buildPlugin") {
        dependsOn("copyInspections")
    }
    named("runIde") {
        dependsOn("copyInspections")
    }
}
tasks.getByName("buildSearchableOptions").onlyIf { false }

// Custom functions
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

