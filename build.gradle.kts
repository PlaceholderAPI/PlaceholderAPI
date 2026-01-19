import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "at.helpch"
version = "1.0.0"

description = "An awesome placeholder provider!"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
//    compileOnly files("libs/HytaleServer.jar")
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25

    withJavadocJar()
    withSourcesJar()

    disableAutoTargetJvm()
}