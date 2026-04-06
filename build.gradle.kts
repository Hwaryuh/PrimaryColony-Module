plugins {
    java
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
    id("com.gradleup.shadow") version "8.3.6"
}

group = "studio.semicolon"
version = "1.0.5"
val serverDirectory = project.findProperty("server.directory") as String

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    implementation(files("libs/Quill-0.2.86.jar"))
    compileOnly(files("libs/PRCMission-1.0.9.jar"))

    compileOnly("com.github.mireu9275:PRCShop:v1.1.1")
}

tasks {
    compileJava {
        options.release = 21
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        archiveClassifier.set("")
        configurations = listOf(project.configurations.runtimeClasspath.get())
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    build {
        dependsOn(shadowJar)
        doLast {
            val jar =
                shadowJar
                    .get()
                    .archiveFile
                    .get()
                    .asFile
            val serverDir = file(serverDirectory)
            val updateDir = File(serverDir, "update")

            updateDir.mkdirs()
            jar.copyTo(File(updateDir, jar.name), overwrite = true)
        }
    }
}
