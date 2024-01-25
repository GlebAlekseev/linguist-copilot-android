@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("conventionPluginsApp.android.library")
}

android {
    namespace = "pro.linguistcopilot.core.book"
}

dependencies {
    implementation(project(":core:utils"))
    implementation(libs.jsoup)
}