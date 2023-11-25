@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("conventionPluginsApp.android.library")
}

android {
    namespace = "pro.linguistcopilot.features.splash"
}

dependencies {
    implementation(libs.material)
}