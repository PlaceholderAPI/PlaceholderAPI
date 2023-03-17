import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    java
    `maven-publish`
    id("com.github.hierynomus.license") version "0.15.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "me.clip"
version = "2.11.3-DEV-${System.getProperty("BUILD_NUMBER")}"

description = "An awesome placeholder provider!"

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")

    mavenCentral()
    mavenLocal()

    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation("org.bstats:bstats-bukkit:3.0.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.0")

    compileOnly("org.spigotmc:spigot-api:1.19-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:23.0.0")

    testImplementation("org.openjdk.jmh:jmh-core:1.32")
    testImplementation("org.openjdk.jmh:jmh-generator-annprocess:1.32")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks {
    processResources {
        eachFile { expand("version" to project.version) }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<Javadoc> {
        isFailOnError = false

        with(options as StandardJavadocDocletOptions) {
            addStringOption("Xdoclint:none", "-quiet")
            addStringOption("encoding", "UTF-8")
            addStringOption("charSet", "UTF-8")
        }
    }

    withType<ShadowJar> {
        archiveClassifier.set("")

        relocate("org.bstats", "me.clip.placeholderapi.metrics")
        relocate("net.kyori", "me.clip.placeholderapi.libs.kyori")
    }

    test {
        useJUnitPlatform()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withJavadocJar()
    withSourcesJar()
}

license {
    header = rootProject.file("config/headers/main.txt")
    include("**/*.java")
    encoding = "UTF-8"

    ext {
        set("year", 2021)
    }
}

configurations {
    testImplementation {
        extendsFrom(compileOnly.get())
    }
}

//publishing {
//    repositories {
//        maven {
//            if (version.contains("-DEV")) {
//                url = uri("https://repo.extendedclip.com/content/repositories/dev/")
//            } else {
//                url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
//            }
//
//            credentials {
//                username = System.getenv("JENKINS_USER")
//                password = System.getenv("JENKINS_PASS")
//            }
//        }
//    }
//
//    publications {
//        mavenJava(MavenPublication) {
//            artifactId = "placeholderapi"
//
//            from components.java
//
//            pom.withXml {
//
//                // some are having issues with bstats so we might need to add that to the pom as well
//
//                asNode().appendNode("packaging", "jar")
//                asNode().remove(asNode().get("dependencies"))
//
//                def dependenciesNode = asNode().appendNode("dependencies")
//                // jetbrains annotations
//                def jetbrainsAnnotations = dependenciesNode.appendNode("dependency")
//                jetbrainsAnnotations.appendNode("groupId", "org.jetbrains")
//                jetbrainsAnnotations.appendNode("artifactId", "annotations")
//                jetbrainsAnnotations.appendNode("version", "19.0.0")
//            }
//        }
//    }
//}
//
//publish.dependsOn clean, test, jar
