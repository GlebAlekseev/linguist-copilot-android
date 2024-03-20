plugins {
    id("linguistcopilot.feature.impl")
    alias(libs.plugins.kotlin.serialization)
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