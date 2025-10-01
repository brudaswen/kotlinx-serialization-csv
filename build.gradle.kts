import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

plugins {
    kotlin("jvm") version libs.versions.kotlin apply false
    kotlin("plugin.serialization") version libs.versions.kotlin apply false
    alias(libs.plugins.nexus.publish)
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

nexusPublishing {
    packageGroup = "de.brudaswen"

    clientTimeout = 5.minutes.toJavaDuration()

    repositories {
        sonatype()
    }
}
