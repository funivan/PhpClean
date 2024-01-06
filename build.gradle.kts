import org.jetbrains.changelog.Changelog
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("org.jetbrains.intellij") version "1.16.1"
    id("org.jetbrains.changelog") version "2.2.0"
    // ktlint linter - read more: https://github.com/JLLeitschuh/ktlint-gradle
    // id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
}
fun properties(key: String) = project.findProperty(key).toString()

// Import variables from gradle.properties file
val pluginGroup: String by project
val appPluginName: String by project
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
}

// Configure gradle-intellij-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set(appPluginName)
    version.set(platformVersion)
    type.set(platformType)
    downloadSources.set(platformDownloadSources.toBoolean())
    updateSinceUntilBuild.set(true)
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
    properties("javaVersion").let {
        withType<JavaCompile> {
            sourceCompatibility = it
            targetCompatibility = it
        }
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = it
        }
    }

    publishPlugin {
        token.set(intellijPublishToken)
        channels.set(listOf(intellijPublishChannel, "default").filter(String::isNotEmpty).take(1))
    }
    patchPluginXml {
        version.set(pluginVersion)
        sinceBuild.set(pluginSinceBuild)
        changeNotes.set(
            changelog.renderItem(changelog.getLatest(), Changelog.OutputType.HTML)
        )
    }

    runPluginVerifier {
        ideVersions.set(
            properties("pluginVerifierIdeVersions").split(',').map(String::trim).filter(String::isNotEmpty)
        )
    }

    register("copyFiles") {
        doLast {
            intentions().forEach {
                val dir = "src/main/resources/intentionDescriptions/" + it.name()
                write(File("$dir/description.html"), it.content())
                write(File("$dir/after.php.template"), it.read("after.php.template"))
                write(File("$dir/before.php.template"), it.read("before.php.template"))
            }
            inspections().forEach {
                write(
                    File("src/main/resources/inspectionDescriptions/" + it.name() + "Inspection.html"), it.content()
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

    named("test") {
        dependsOn("checkReadme")
    }
    named("buildPlugin") {
        dependsOn("copyFiles")
        dependsOn("updateReadme")
    }
    named("runIde") {
        dependsOn("updateReadme")
        dependsOn("copyFiles")
    }
}
tasks.getByName("buildSearchableOptions").onlyIf { false }

// Custom functions
fun write(file: File, content: String): Boolean {
    var result = false
    val parent = file.parentFile
    if (parent != null && !parent.exists()) {
        parent.mkdirs()
    }
    if (!file.exists()) {
        file.createNewFile()
    }
    if (file.readText() != content) {
        result = true
        file.writeText(content)
    }
    return result
}

class Document(
    private val file: File, private val name: String
) {
    fun name() = name
    fun content() = file.readText()
    fun short() = content().replace(Regex("<!-- main -->(.*)", RegexOption.DOT_MATCHES_ALL), "").trim()
    fun read(name: String) = File(file.parentFile, name).readText()
}

fun actions() = projectHtmlFiles("actions")
fun inspections() = projectHtmlFiles("inspections")
fun intentions() = intentionFiles()


fun generatedReadmeContent(readme: File): String = readme.readText().replace(
    Regex("<!-- Autogenerated -->.+", RegexOption.DOT_MATCHES_ALL), ""
) + "<!-- Autogenerated -->\n" + generateSections()

fun readmeFile() = File("README.md")
fun projectHtmlFiles(type: String) =
    File("src/main/kotlin/com/funivan/idea/phpClean/$type").walkTopDown().filter { it.extension == "html" }.map {
        Document(File(it.path), it.name)
    }

fun intentionFiles() =
    File("src/main/kotlin/com/funivan/idea/phpClean/intentions").walkTopDown().filter { it.name == "description.html" }
        .map {
            val name = it.parentFile.parentFile.name + "Intention"
            Document(
                File(it.path),
                name.substring(0, 1).uppercase(Locale.getDefault()) + name.substring(1)
            )
        }

fun generateSections() = listOf(
    Pair("Inspections", inspections()),
    Pair("Actions", actions()),
    Pair("Intentions", intentions()),
).fold("") { acc, pair ->
    acc + "## ${pair.first}\n" + pair.second.sortedBy { it.name() }.map {
        val description = it.short().replace("<pre>", "```php").replace("</pre>", "```")
        "#### ${it.name()}\n$description\n"
    }.joinToString("") + "\n"
}
