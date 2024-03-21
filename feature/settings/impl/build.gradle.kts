import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Properties

plugins {
    id("linguistcopilot.feature.impl")
    alias(libs.plugins.kotlin.serialization)
}



android {
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        val localProps = Properties()
        try {
            localProps.load(FileInputStream(rootProject.file("local.properties")))
        } catch (e: FileNotFoundException) {

        }
        buildConfigField("String", "DEEPL_FREE_API_KEY", "\"${localProps["deepl_free_api_key"] ?: ""}\"")
        buildConfigField("String", "DEEPL_PRO_API_KEY", "\"${localProps["deepl_pro_api_key"] ?: ""}\"")
        buildConfigField("String", "MYMEMORY_EMAIL", "\"${localProps["mymemory_email"] ?: ""}\"")
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)

    androidTestImplementation(libs.androidx.testExtJunit)
    testImplementation(libs.junit)

    implementation(libs.androidx.coreKtx)
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
    implementation(libs.androidx.appCompat)

    implementation(project(":core:di"))
    api(project(":feature:settings:api"))
    api(project(":app:api"))

    implementation("androidx.datastore:datastore-preferences:1.0.0")

}