@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("conventionPluginsApp.android.library")
}

android {
    namespace = "pro.linguistcopilot.navigation"
}

dependencies {
    implementation(libs.navigation.fragment)
}