plugins {
    alias(libs.plugins.runPaper)
    alias(libs.plugins.shadow)

    id("paper-plugin")
}

project.version = rootProject.version

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

    maven("https://repo.oraxen.com/releases/")
}

dependencies {
    implementation(libs.vital.paper)

    implementation(libs.nbt.api)

    compileOnly(libs.headdatabaseapi)

    compileOnly(libs.placeholderapi)

    //compileOnly(libs.itemsadder)

    compileOnly(libs.oraxen)
}

val component: SoftwareComponent = components["java"]

tasks {
    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(libs.versions.minecraft.get())
    }

    assemble {
        dependsOn(shadowJar)

        doLast {
            copy {
                from(shadowJar.get())
                into(rootProject.projectDir.resolve("jars"))
            }
        }
    }

    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")

        listOf(
            "com.ryderbelserion.vital",
            "de.tr7zw.changeme.nbtapi"
        ).forEach {
            relocate(it, "libs.$it")
        }
    }

    processResources {
        inputs.properties("name" to rootProject.name)
        inputs.properties("version" to project.version)
        inputs.properties("group" to rootProject.group)
        inputs.properties("authors" to rootProject.properties["authors"].toString())
        inputs.properties("apiVersion" to libs.versions.minecraft.get())
        inputs.properties("description" to rootProject.description)
        inputs.properties("website" to rootProject.properties["website"].toString())

        filesMatching("plugin.yml") {
            expand(inputs.properties)
        }
    }

    publishing {
        repositories {
            maven {
                url = uri("https://repo.crazycrew.us/releases")

                credentials {
                    this.username = System.getenv("gradle_username")
                    this.password = System.getenv("gradle_password")
                }
            }
        }

        publications {
            create<MavenPublication>("maven") {
                groupId = rootProject.group.toString()
                artifactId = "${rootProject.name.lowercase()}-${project.name.lowercase()}-api"
                version = rootProject.version.toString()

                from(component)
            }
        }
    }
}