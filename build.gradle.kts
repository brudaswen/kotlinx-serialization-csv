plugins {
    base
    kotlin("jvm") version "1.3.72" apply false
    id("net.researchgate.release") version "2.6.0"
    id("io.codearte.nexus-staging") version "0.21.2"
}

val serializationVersion = "0.20.0"

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
