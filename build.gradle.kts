import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
    id("com.github.hierynomus.license") version "0.16.1"
    id("com.github.johnrengelman.shadow") version "8.1.0"
}

group = "me.clip"
version = "2.11.4-DEV-${System.getProperty("BUILD_NUMBER")}"

description = "An awesome placeholder provider!"

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")

    mavenCentral()
    mavenLocal()

    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation("org.bstats:bstats-bukkit:3.0.1")
    implementation("net.kyori:adventure-platform-bukkit:4.3.0")

    compileOnly("org.spigotmc:spigot-api:1.20-R0.1-SNAPSHOT")
    compileOnlyApi("org.jetbrains:annotations:23.0.0")

    testImplementation("org.openjdk.jmh:jmh-core:1.32")
    testImplementation("org.openjdk.jmh:jmh-generator-annprocess:1.32")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
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
    mapping("java", "JAVADOC_STYLE")

    encoding = "UTF-8"

    ext {
        set("year", 2021)
    }
}

val javaComponent: SoftwareComponent = components["java"]

tasks {
    processResources {
        eachFile { expand("version" to project.version) }
    }

    build {
        dependsOn(named("shadowJar"))
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
                    url = uri("https://repo.extendedclip.com/content/repositories/dev/")
                } else {
                    url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
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

configurations {
    testImplementation {
        extendsFrom(compileOnly.get())
    }
}
