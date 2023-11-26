@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("conventionPluginsApp.android.library")
}

android {
    namespace = "pro.linguistcopilot.core.featureToggles"
}

dependencies {
    implementation(libs.material)
    implementation(libs.gson.converter)
    implementation(project(":core:utils"))
}