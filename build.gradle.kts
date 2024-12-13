plugins {
    base
    kotlin("jvm") version libs.versions.kotlin apply false
    kotlin("plugin.serialization") version libs.versions.kotlin apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.nexus.publish) apply false
    alias(libs.plugins.researchgate.release)
    alias(libs.plugins.nexus.staging)
}

allprojects {
    group = "de.brudaswen.kotlinx.serialization"

    repositories {
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
    configure: net.researchgate.release.GitAdapter.GitConfig.() -> Unit,
) = (getProperty("git") as net.researchgate.release.GitAdapter.GitConfig).configure()
