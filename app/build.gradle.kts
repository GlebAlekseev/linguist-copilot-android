@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("linguistcopilot.android.application")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "pro.linguistcopilot"

    defaultConfig {
        applicationId = "pro.linguistcopilot"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources.excludes += setOf("META-INF/**")
    }
}

dependencies {
    implementation(libs.decompose)
    implementation(libs.decompose.extensionsCompose)
    implementation(libs.androidx.coreKtx)
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
    implementation(libs.androidx.appCompat)
    implementation(project(":core:utils"))
    implementation(project(":design:res"))
}
