@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("linguistcopilot.android.library")

}

dependencies {
    implementation(libs.material)
    implementation(libs.dagger)
    implementation(libs.hutool.crypto)
}