@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("conventionPluginsApp.android.library")
    id("conventionPluginsApp.android.library.presentationFeature")
    id("kotlin-parcelize")
}

android {
    namespace = "pro.linguistcopilot.features.reader"
}

dependencies {
    implementation(libs.material)
    implementation(libs.gson.converter)
    implementation(project(":navigation"))
    implementation(project(":core:utils"))
    implementation(project(":core:book"))
    implementation(project(":design"))
    implementation(project(":features:reader:domain"))
}