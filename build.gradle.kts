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
val fileName = "$name.jar"
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
    archiveName = fileName
}

tasks.register<Copy>("patchRepositoryXml") {
    doFirst {
        delete("${buildDir}/libs/*.xml")
    }
    from("src/ci/PhpClean-nightly.xml")
    into("$buildDir/libs")
    expand(hashMapOf(
            "pluginName" to project.property("pluginName"),
            "fileSize" to "18000",
            "version" to project.property("version").toString(),
            "pluginName" to project.property("pluginName"),
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