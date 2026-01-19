import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "9.3.1"
}

group = "at.helpch"
version = "1.0.0"

description = "An awesome placeholder provider!"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.yaml:snakeyaml:2.5")

    compileOnly(files("libs/HytaleServer.jar"))
    compileOnlyApi("org.jetbrains:annotations:23.0.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25

    withJavadocJar()
    withSourcesJar()

    disableAutoTargetJvm()
}