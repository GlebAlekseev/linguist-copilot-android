@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("conventionPluginsApp.android.library")
}

android {
    namespace = "pro.linguistcopilot.core.utils"
}

dependencies {
    implementation(libs.material)
    implementation(libs.dagger)
    implementation(libs.hutool.crypto)
}