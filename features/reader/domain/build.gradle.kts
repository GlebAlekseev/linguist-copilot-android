@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("conventionPluginsApp.android.library")
}

android {
    namespace = "pro.linguistcopilot.features.reader.domain"
}

dependencies {
    implementation(project(":core:utils"))
}