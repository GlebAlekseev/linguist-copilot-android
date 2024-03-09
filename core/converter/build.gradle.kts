@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("linguistcopilot.android.library")

}

dependencies {
    androidTestImplementation(libs.androidx.testExtJunit)
    testImplementation(libs.junit)
    implementation(libs.dagger)

    implementation(project(":core:python"))
    implementation(project(":core:utils"))
}