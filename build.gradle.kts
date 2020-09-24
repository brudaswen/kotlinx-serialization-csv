plugins {
    base
    kotlin("jvm") version "1.4.10" apply false
    id("net.researchgate.release") version "2.8.1"
    id("io.codearte.nexus-staging") version "0.22.0"
}

val serializationVersion = "1.0.0-RC"

allprojects {
    group = "de.brudaswen.kotlinx.serialization"

    extra["serializationVersion"] = serializationVersion

    repositories {
        jcenter()
        mavenCentral()
    }
}

// Use `./gradlew release` to create a tagged release commit
release {
    preTagCommitMessage = "[Gradle Release Plugin] Release version"
    tagCommitMessage = "[Gradle Release Plugin] Release version"
    newVersionCommitMessage = "[Gradle Release Plugin] New version"

    git {
        requireBranch = "main"
    }
}

val mavenCentralUsername: String? by project
val mavenCentralPassword: String? by project
nexusStaging {
    packageGroup = "de.brudaswen"
    username = mavenCentralUsername
    password = mavenCentralPassword
    numberOfRetries = 60
    delayBetweenRetriesInMillis = 10_000
}

fun net.researchgate.release.ReleaseExtension.git(
    configure: net.researchgate.release.GitAdapter.GitConfig.() -> Unit
) = (getProperty("git") as net.researchgate.release.GitAdapter.GitConfig).configure()
