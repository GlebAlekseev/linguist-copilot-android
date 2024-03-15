plugins {
    id("linguistcopilot.feature.api")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.dagger)
    implementation(libs.kotlinx.coroutinesCore)
    api(project(":core:utils"))
    api(project(":feature:textProcessing:api"))
}