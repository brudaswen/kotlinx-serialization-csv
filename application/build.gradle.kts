plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.3.61"
}

val implementation by configurations

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":library"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
