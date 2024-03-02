@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("linguistcopilot.android.library")

}

dependencies {
    androidTestImplementation(libs.androidx.testExtJunit)
    testImplementation(libs.junit)

    implementation(libs.material)
    implementation(libs.dagger)
    implementation(libs.hutool.crypto)

    implementation(libs.decompose)
    implementation(libs.decompose.extensionsCompose)
    implementation(libs.elmslie.core)
    implementation(libs.elmslie.coroutines)
}