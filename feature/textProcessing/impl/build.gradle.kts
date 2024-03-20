plugins {
    id("linguistcopilot.feature.impl")
    alias(libs.plugins.kotlin.serialization)
}


dependencies {
    androidTestImplementation(libs.androidx.testExtJunit)
    testImplementation(libs.junit)
    implementation(libs.androidx.coreKtx)
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
    implementation(project(":core:di"))

    api(project(":feature:textProcessing:api"))
    implementation(project(":feature:word:api"))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}