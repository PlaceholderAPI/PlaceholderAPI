import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation(project(":spigot")) {
        exclude("net.kyori")
    }
    compileOnly("net.kyori:adventure-platform-bukkit:4.4.1")
}

val javaComponent: SoftwareComponent = components["java"]

base {
    archivesName.set("PlaceholderAPI-Paper")
}

tasks {
    withType<ShadowJar> {
        archiveClassifier.set("")
        archiveBaseName.set("PlaceholderAPI-Paper")

        relocate("org.bstats", "me.clip.placeholderapi.metrics")

        exclude("META-INF/versions/**")
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = "placeholderapi-paper"
                from(javaComponent)
            }
        }

        repositories {
            maven {
                if ("-DEV" in version.toString()) {
                    url = uri("https://repo.extendedclip.com/snapshots")
                } else {
                    url = uri("https://repo.extendedclip.com/releases")
                }

                credentials {
                    username = System.getenv("JENKINS_USER")
                    password = System.getenv("JENKINS_PASS")
                }
            }
        }
    }

    publish.get().setDependsOn(listOf(build.get()))
}