plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    alias(libs.plugins.ktlint)
}

dependencies {
    implementation(project(":library"))
}

kotlin {
    jvmToolchain(21)
}
