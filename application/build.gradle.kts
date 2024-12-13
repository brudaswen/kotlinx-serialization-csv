plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":library"))
}

kotlin {
    jvmToolchain(jdkVersion = 8)
}
