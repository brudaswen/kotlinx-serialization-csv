plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    alias(libs.plugins.dokka.javadoc)
    alias(libs.plugins.ktlint)
    `maven-publish`
    signing
    alias(libs.plugins.kover)
}

dependencies {
    commonMainApi(libs.kotlinx.serialization.core)
    commonMainApi("org.jetbrains.kotlinx:kotlinx-io-core:0.8.0")

    commonTestImplementation(kotlin("test"))
    commonTestImplementation(libs.kotlinx.coroutines.test)
    commonTestImplementation("org.jetbrains.kotlinx:kotlinx-io-bytestring:0.8.0")
    commonTestImplementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
}

kotlin {
    jvm()
    iosArm64()
    iosSimulatorArm64()
    macosArm64()

    jvmToolchain(8)

    explicitApi()

    @OptIn(org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation::class)
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
    archiveClassifier.set("javadoc")
    from(tasks.dokkaGeneratePublicationJavadoc)
}

publishing {
    publications {
        create<MavenPublication>("library") {
            artifactId = "kotlinx-serialization-csv"

            pom {
                name.set("kotlinx-serialization-csv")
                description.set("Library to easily use Kotlin Serialization to serialize to/from CSV.")
                url.set("https://github.com/brudaswen/serialization-csv/")

                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("brudaswen")
                        name.set("Sven Obser")
                        email.set("dev@brudaswen.de")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/brudaswen/kotlinx-serialization-csv.git")
                    developerConnection.set("scm:git:ssh://git@github.com:brudaswen/kotlinx-serialization-csv.git")
                    url.set("https://github.com/brudaswen/kotlinx-serialization-csv/")
                }
                issueManagement {
                    system.set("GitHub Issues")
                    url.set("https://github.com/brudaswen/kotlinx-serialization-csv/issues/")
                }
            }

            from(components["kotlin"])
            artifact(dokkaJavadocJar)
        }
    }
}

signing {
    setRequired { !isSnapshot() }

    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)

    sign(publishing.publications["library"])
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
