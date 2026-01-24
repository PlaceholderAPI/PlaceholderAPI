import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "9.3.1"
}

group = "at.helpch"
version = "1.0.0-experifuckingmental"

description = "An awesome placeholder provider!"

repositories {
    mavenCentral()
    mavenLocal()

    maven {
        url = uri("https://repo.codemc.io/repository/hytale/")
    }
}

dependencies {
    implementation("org.yaml:snakeyaml:2.5")

    compileOnly("com.hypixel.hytale:Server:2026.01.17-4b0f30090")
    compileOnlyApi("org.jetbrains:annotations:23.0.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    withJavadocJar()
    withSourcesJar()

    disableAutoTargetJvm()
}

tasks {
    processResources {
        eachFile { expand("version" to project.version) }
    }
}