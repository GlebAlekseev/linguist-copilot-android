@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("linguistcopilot.android.library")
}


dependencies {
    implementation(project(":core:utils"))
    implementation(libs.jsoup)
    implementation(libs.hutool.crypto)
    implementation(libs.splitties.appCtx)
}