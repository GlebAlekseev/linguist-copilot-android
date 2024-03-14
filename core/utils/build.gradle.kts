@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("linguistcopilot.kotlin.library")
}

dependencies {
    testImplementation(libs.junit)
    implementation(libs.kotlinx.coroutinesCore)
    implementation(libs.apache.commons.codec)
}