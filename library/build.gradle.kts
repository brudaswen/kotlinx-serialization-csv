plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    alias(libs.plugins.dokka.javadoc)
    `maven-publish`
    signing
    jacoco
}

dependencies {
    api(libs.kotlinx.serialization.core)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotlinx.coroutines.test)
}

kotlin {
    jvmToolchain(8)
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

tasks.register("publishRelease") {
    group = "publishing"
    description = "Publish to Maven Central (iff this is a release version)."

    onlyIf { !isSnapshot() }

    if (!isSnapshot()) {
        dependsOn("publishToSonatype", "closeAndReleaseStagingRepositories")
    }
}

tasks.register("publishSnapshot") {
    group = "publishing"
    description = "Publish to Maven Central (iff this is a snapshot version)."

    onlyIf { isSnapshot() }

    if (isSnapshot()) {
        dependsOn("publishToSonatype")
    }
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

            from(components["java"])
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

tasks.jacocoTestReport {
    reports {
        xml.required = true
        html.required = false
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
}

fun isSnapshot() = version.toString().endsWith("-SNAPSHOT")
