import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
//    id("com.github.hierynomus.license") version "0.16.1"
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "me.clip"
version = "2.12.3-DEV-${System.getProperty("BUILD_NUMBER")}"

description = "An awesome placeholder provider!"

val paper by sourceSets.creating {
    java.srcDir("src/paper/java")

    // paper can see main code
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += output + compileClasspath
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")

    mavenCentral()
    mavenLocal()

    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation("org.bstats:bstats-bukkit:3.1.0")
    implementation("net.kyori:adventure-platform-bukkit:4.4.1")

    add(paper.compileOnlyConfigurationName, "net.kyori:adventure-platform-bukkit:4.4.1")
    add(paper.compileOnlyConfigurationName, "dev.folia:folia-api:1.21.11-R0.1-SNAPSHOT")

    compileOnly("dev.folia:folia-api:1.21.11-R0.1-SNAPSHOT")
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

    disableAutoTargetJvm()
}

val javaComponent: SoftwareComponent = components["java"]

tasks {
    processResources {
        eachFile { expand("version" to project.version) }
    }

    build {
        dependsOn(named("shadowJar"))
    }

    register<JavaCompile>("compilePaper") {
        source = paper.java
        classpath = paper.compileClasspath
        destinationDirectory.set(layout.buildDirectory.dir("classes/java/paper"))
        options.encoding = "UTF-8"
        options.release = 8
    }

    val plainJar by registering(Jar::class) {
        dependsOn("compilePaper")

        archiveClassifier.set("plain")
        from(sourceSets.main.get().output)
        from(paper.output)
    }

    val combinedSourcesJar by registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
        from(paper.allSource)

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    val combinedJavadoc by registering(Javadoc::class) {
        isFailOnError = false

        source = sourceSets.main.get().allJava + paper.allJava
        classpath = sourceSets.main.get().compileClasspath + paper.compileClasspath

        with(options as StandardJavadocDocletOptions) {
            addStringOption("Xdoclint:none", "-quiet")
            addStringOption("encoding", "UTF-8")
            addStringOption("charSet", "UTF-8")
        }
    }

    val combinedJavadocJar by registering(Jar::class) {
        archiveClassifier.set("javadoc")
        dependsOn(combinedJavadoc)
        from(combinedJavadoc.get().destinationDir)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release = 8
    }

    withType<ShadowJar> {
        configurations = listOf(project.configurations.runtimeClasspath.get())

        from(sourceSets.main.get().output)

        archiveClassifier.set("")

        relocate("org.bstats", "me.clip.placeholderapi.metrics")
        relocate("net.kyori", "me.clip.placeholderapi.libs.kyori")

        exclude("META-INF/versions/**")

        dependsOn("compilePaper")

        doLast {
            val paperDir = layout.buildDirectory.dir("classes/java/paper").get().asFile
            val jarFile = archiveFile.get().asFile

            ant.invokeMethod("zip", mapOf(
                "destfile" to jarFile,
                "update" to "true",
                "basedir" to paperDir
            ))
        }
    }

    test {
        useJUnitPlatform()
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = "placeholderapi"

                artifact(plainJar) {
                    builtBy(plainJar)
                    classifier = ""
                }

                artifact(combinedSourcesJar) {
                    builtBy(combinedSourcesJar)
                }

                artifact(combinedJavadocJar) {
                    builtBy(combinedJavadocJar)
                }
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

configurations {
    testImplementation {
        extendsFrom(compileOnly.get())
    }
}