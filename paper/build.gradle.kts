import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    api(project(":spigot"))
}

val javaComponent: SoftwareComponent = components["java"]

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