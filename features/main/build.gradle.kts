@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("conventionPluginsApp.android.library")
    id("conventionPluginsApp.android.presentationFeature")
}

android {
    namespace = "pro.linguistcopilot.features.main"
}

dependencies {
    implementation(libs.material)
    implementation(project(":navigation"))
    implementation(project(":core:utils"))
}