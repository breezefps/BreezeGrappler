import me.dkim19375.dkimgradle.enums.mavenAll
import me.dkim19375.dkimgradle.util.getVersionString
import me.dkim19375.dkimgradle.util.setupTasksForMC

plugins {
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.serialization") version "1.8.22"

    id("org.cadixdev.licenser") version "0.6.1"
    id("io.papermc.paperweight.userdev") version "1.5.5"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.github.dkim19375.dkim-gradle") version "1.2.1"
}

group = "me.dkim19375"
version = "1.0.0"

license {
    header.set(rootProject.resources.text.fromFile("HEADER"))
    include("**/*.kt")
}

repositories {
    mavenAll()
}

val pluginYMLDependencies = mutableSetOf<String>()

dependencies {
    paperweight.paperDevBundle(getVersionString("1.20.1"))

    compileOnly("me.clip:placeholderapi:2.11.2")
    compileOnly("org.jetbrains:annotations:23.1.0")
    compileOnly("dev.jorel:commandapi-bukkit-kotlin:9.0.3")

    compileForSpigot("net.kyori:adventure-extra-kotlin:4.14.0")
    compileForSpigot("dev.jorel:commandapi-bukkit-kotlin:9.0.3")
    compileForSpigot("org.jetbrains.kotlin:kotlin-stdlib:1.8.22")
    compileForSpigot("org.jetbrains.kotlin:kotlin-reflect:1.8.22")
    compileForSpigot("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")

    implementation("com.charleskorn.kaml:kaml:0.54.0")
    implementation("io.github.dkim19375:dkim-bukkit-core:3.4.1"){
        exclude(group = "org.jetbrains.kotlin")
        exclude(group = "org.jetbrains.kotlinx")
    }
}

kotlin {
    jvmToolchain(17)
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }

    setupTasksForMC(
        serverFoldersRoot = "../.TestServers",
        serverFolderNames = setOf("1.20"),
        mainServerName = "1.20",
        jarFileName = shadowJar.get().archiveBaseName.get(),
        jar = { reobfJar.get().outputJar.get().asFile },
        replacements = mapOf(
            "version" to version::toString,
            "libs" to {
                pluginYMLDependencies.joinToString("\n") { "  - $it" }.let {
                    if (it.isNotBlank()) "libraries:\n$it" else ""
                }
            },
        ),
        javaVersion = "17",
    )

    shadowJar {
        val relocations = setOf(
            "com.charleskorn.kaml",
            "me.dkim19375.dkimcore",
            "org.jetbrains.annotations",
            "me.dkim19375.dkimbukkitcore",
            "org.intellij.lang.annotations",
        )
        val exclude = setOf(
            "org.jetbrains.kotlin:",
            "org.jetbrains.kotlinx:",
            "org.jetbrains:annotations",
        )
        for (relocation in relocations) {
            relocate(relocation, "me.dkim19375.breezegrappler.libs.$relocation")
        }
        dependencies {
            for (exclusion in exclude) {
                exclude(dependency(exclusion))
            }
        }
    }
}

fun DependencyHandler.compileForSpigot(
    dependencyNotation: String,
    dependencyConfiguration: ExternalModuleDependency.() -> Unit = {},
): Dependency {
    pluginYMLDependencies.add(dependencyNotation)
    return compileOnly(dependencyNotation, dependencyConfiguration)
}