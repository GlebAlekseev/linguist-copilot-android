@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("linguistcopilot.android.library")
}


dependencies {
    androidTestImplementation(libs.androidx.testExtJunit)
    testImplementation(libs.junit)

    implementation(project(":core:crypto"))
    implementation(libs.jsoup)
    implementation(libs.hutool.crypto)
    implementation(libs.splitties.appCtx)
}