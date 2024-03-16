@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("linguistcopilot.android.application")
    id("linguistcopilot.kotlin.parcelize")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "pro.linguistcopilot.core.python.testapp"

    defaultConfig {
        applicationId = "pro.linguistcopilot.core.python.testapp"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
//    packaging {
//        resources.excludes += setOf("META-INF/**")
//    }
    packaging {
        resources.excludes += setOf("META-INF/INDEX.LIST")
    }
}

dependencies {
    implementation(libs.androidx.coreKtx)
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
    implementation(libs.androidx.appCompat)
    api(project(":app:api"))
    implementation(project(":core:di"))
    implementation(project(":core:python"))
    implementation(project(":design:res"))
}
