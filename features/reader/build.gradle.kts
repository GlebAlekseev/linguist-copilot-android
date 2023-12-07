@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("conventionPluginsApp.android.library")
    id("conventionPluginsApp.android.library.presentationFeature")
}

android {
    namespace = "pro.linguistcopilot.features.reader"
}

dependencies {
    implementation(libs.material)
    implementation(project(":navigation"))
    implementation(project(":core:utils"))
    implementation(project(":design"))
    implementation(project(":features:reader:domain"))
}