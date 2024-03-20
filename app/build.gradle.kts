@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("linguistcopilot.android.application")
    id("linguistcopilot.kotlin.parcelize")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "pro.linguistcopilot"

    defaultConfig {
        applicationId = "pro.linguistcopilot"
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
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.decompose)
    implementation(libs.decompose.extensionsCompose)
    implementation(libs.elmslie.core)
    implementation(libs.elmslie.coroutines)
    implementation(libs.androidx.coreKtx)
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
    implementation(libs.androidx.appCompat)
    api(project(":app:api"))
    implementation(project(":core:di"))
    implementation(project(":core:python"))
    implementation(project(":design:res"))
    implementation(project(":feature:onboarding:impl"))
    implementation(project(":feature:auth:impl"))
    implementation(project(":feature:content:impl"))
    implementation(project(":feature:bookDownload:impl"))
    implementation(project(":feature:bookDescription:impl"))
    implementation(project(":feature:bookReader:impl"))
    implementation(project(":feature:bookSearch:impl"))
    implementation(project(":feature:book:impl"))
    implementation(project(":feature:textProcessing:impl"))
    implementation(project(":feature:translate:impl"))
    implementation(project(":feature:transcription:impl"))
    implementation(project(":feature:word:impl"))
}
