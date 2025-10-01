import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

plugins {
    kotlin("jvm") version libs.versions.kotlin apply false
    kotlin("plugin.serialization") version libs.versions.kotlin apply false
    alias(libs.plugins.nexus.publish)
    alias(libs.plugins.nexus.staging)
    alias(libs.plugins.researchgate.release)
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

nexusPublishing {
    repositories {
        sonatype()
    }

    clientTimeout = 30.minutes.toJavaDuration()

    val useSnapshot: String? by project
    if (useSnapshot != null) {
        useStaging.set(useSnapshot?.toBoolean()?.not())
    }
}
