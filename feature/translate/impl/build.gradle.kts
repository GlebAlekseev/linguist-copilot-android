plugins {
    id("linguistcopilot.feature.impl")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization.json)

    androidTestImplementation(libs.androidx.testExtJunit)
    testImplementation(libs.junit)
    
    implementation(libs.androidx.coreKtx)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.gson)
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
    implementation(libs.androidx.appCompat)

    implementation(project(":core:di"))
    api(project(":feature:translate:api"))
    implementation(project(":feature:word:api"))

    implementation("com.google.mlkit:translate:17.0.2")
}