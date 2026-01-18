import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val javaComponent: SoftwareComponent = components["java"]

base {
    archivesName.set("PlaceholderAPI-Spigot")
}

tasks {
    withType<ShadowJar> {
        archiveClassifier.set("")
        archiveBaseName.set("PlaceholderAPI-Spigot")

        relocate("org.bstats", "me.clip.placeholderapi.metrics")
        relocate("net.kyori", "me.clip.placeholderapi.libs.kyori")

        exclude("META-INF/versions/**")
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = "placeholderapi"
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