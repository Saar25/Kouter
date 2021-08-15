import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"

    `maven-publish`
    signing
}

object Info {
    const val groupId = "com.github.saar25"
    const val artifactId = "kouter"
    const val version = "1.0.0"
    const val path = "github.com/Saar25/Kouter"
}

group = Info.groupId
version = Info.version

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = Info.groupId
            artifactId = Info.artifactId
            version = Info.version

            from(components["java"])

            pom {
                name.set("Kouter")
                description.set("A lightweight path routing library in kotlin")
                url.set("https://${Info.path}")
                packaging = "jar"

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("saar25")
                        name.set("Saar")
                    }
                }
                scm {
                    connection.set("scm:git:https://${Info.path}.git")
                    developerConnection.set("scm:git:ssh://${Info.path}.git")
                    url.set("https://${Info.path}")
                }
            }
        }
    }

    repositories {
        mavenLocal()
        maven {
            name = "sonatype"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = project.findProperty("ossrhUsername")!!.toString()
                password = project.findProperty("ossrhPassword")!!.toString()
            }
        }
    }
}

signing {
    sign(publishing.publications.getByName("maven"))
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.21")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "15"
}