plugins {
    id("linguistcopilot.feature.api")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.dagger)
    implementation(libs.kotlinx.coroutinesCore)
    implementation(project(":feature:word:api"))
    implementation(project(":feature:textProcessing:api"))
    api(project(":core:utils"))
}