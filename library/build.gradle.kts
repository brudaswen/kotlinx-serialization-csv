import java.time.Duration

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka") version "0.9.18"
    id("de.marcphilipp.nexus-publish") version "0.4.0"
    `maven-publish`
    signing
    jacoco
}

val serializationVersion = extra["serializationVersion"]

val api by configurations
val implementation by configurations
val compileOnly by configurations
val testImplementation by configurations

dependencies {
    api(kotlin("stdlib-jdk8"))
    api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
}

java {
    withSourcesJar()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<GenerateModuleMetadata> {
    enabled = !isSnapshot()
}

val dokkaJavadoc by tasks.creating(org.jetbrains.dokka.gradle.DokkaTask::class) {
    // TODO Change to "javadoc" as soon as https://youtrack.jetbrains.com/issue/KT-31710 is fixed
    outputFormat = "html"
    outputDirectory = "$buildDir/dokkaJavadoc"
}

val dokkaJavadocJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(dokkaJavadoc)
}

val publishRelease = tasks.create("publishRelease") {
    description = "Publish to Maven Central (iff this is a release version)."
}

val publishSnapshot = tasks.create("publishSnapshot") {
    description = "Publish to Maven Central (iff this is a snapshot version)."
}

tasks.whenTaskAdded {
    if (name == "publishToSonatype") {
        val publishToSonatype = this
        if (!isSnapshot()) {
            publishRelease.dependsOn(publishToSonatype)

            val closeAndReleaseRepository = rootProject.tasks.getByName("closeAndReleaseRepository")
            closeAndReleaseRepository.mustRunAfter(publishToSonatype)
            publishRelease.dependsOn(closeAndReleaseRepository)
        } else {
            publishSnapshot.dependsOn(publishToSonatype)
        }
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

nexusPublishing {
    repositories {
        sonatype()
    }

    clientTimeout.set(Duration.ofMinutes(30))
    val useSnapshot: String? by project
    if (useSnapshot != null) {
        useStaging.set(useSnapshot?.toBoolean()?.not())
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
        xml.isEnabled = true
        html.isEnabled = false
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
}

fun isSnapshot() = version.toString().endsWith("-SNAPSHOT")

fun org.jetbrains.dokka.gradle.DokkaTask.externalDocumentationLink(closure: org.jetbrains.dokka.DokkaConfiguration.ExternalDocumentationLink.Builder.() -> Unit) =
    externalDocumentationLink(delegateClosureOf(closure))

fun url(path: String) = uri(path).toURL()
