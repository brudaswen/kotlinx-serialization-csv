@file:OptIn(
    org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class,
    org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation::class,
)

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    alias(libs.plugins.dokka)
    alias(libs.plugins.kover)
    alias(libs.plugins.ktlint)
    `maven-publish`
    signing
}

dependencies {
    commonMainApi(libs.kotlinx.serialization.core)
    commonMainApi(libs.kotlinx.io.core)

    commonTestImplementation(kotlin("test"))
    commonTestImplementation(libs.kotlinx.coroutines.test)
    commonTestImplementation(libs.kotlinx.datetime)
    commonTestImplementation(libs.kotlinx.io.bytestring)
}

kotlin {
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()
    macosX64()
    macosArm64()
    mingwX64()
    wasmJs {
        browser()
        nodejs()
    }

    jvmToolchain(8)

    explicitApi()

    abiValidation {
        enabled = true
    }

    compilerOptions {
        optIn.add("kotlin.uuid.ExperimentalUuidApi")
    }
}

java {
    withSourcesJar()
}

tasks.withType<GenerateModuleMetadata> {
    enabled = !isSnapshot()
}

val dokkaJavadocJar by tasks.registering(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier = "javadoc"
    from(tasks.dokkaGeneratePublicationHtml)
}

publishing {
    publications {
        withType<MavenPublication> {
            artifactId = artifactId.replace(project.name, "kotlinx-serialization-csv")

            if (artifactId.endsWith("-jvm")) {
                artifact(dokkaJavadocJar)
            }

            pom {
                name = "kotlinx-serialization-csv"
                description = "Library to easily use Kotlin Serialization to serialize to/from CSV."
                url = "https://github.com/brudaswen/serialization-csv/"

                licenses {
                    license {
                        name = "Apache License, Version 2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "brudaswen"
                        name = "Sven Obser"
                        email = "dev@brudaswen.de"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/brudaswen/kotlinx-serialization-csv.git"
                    developerConnection =
                        "scm:git:ssh://git@github.com:brudaswen/kotlinx-serialization-csv.git"
                    url = "https://github.com/brudaswen/kotlinx-serialization-csv/"
                }
                issueManagement {
                    system = "GitHub Issues"
                    url = "https://github.com/brudaswen/kotlinx-serialization-csv/issues/"
                }
            }
        }
    }
}

signing {
    setRequired { !isSnapshot() }

    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)

    sign(publishing.publications)
}

kover {
    reports {
        total {
            xml {
                onCheck = true
            }
        }
    }
}

fun isSnapshot() = version.toString().endsWith("-SNAPSHOT")
