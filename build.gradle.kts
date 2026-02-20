import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "9.3.1"
}

group = "at.helpch"
version = "1.0.7-CurseForge"

description = "An awesome placeholder provider!"

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://maven.hytale.com/release/")
}

dependencies {
    implementation("org.yaml:snakeyaml:2.5")

    compileOnly("com.hypixel.hytale:Server:latest.release")
    compileOnlyApi("org.jetbrains:annotations:23.0.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    withJavadocJar()
    withSourcesJar()

    disableAutoTargetJvm()
}

val javaComponent: SoftwareComponent = components["java"]

tasks {
    processResources {
        eachFile { expand("version" to project.version) }
    }

    withType<ShadowJar> {
        archiveClassifier.set("hytale")

        relocate("org.yaml.snakeyaml", "at.helpch.placeholderapi.libs.yaml")

        exclude("META-INF/versions/**")
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = "placeholderapi-hytale"
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