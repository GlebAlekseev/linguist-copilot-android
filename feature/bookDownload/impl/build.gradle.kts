plugins {
    id("linguistcopilot.feature.impl")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    androidTestImplementation(libs.androidx.testExtJunit)
    testImplementation(libs.junit)

    implementation(libs.decompose)
    implementation(libs.decompose.extensionsCompose)
    implementation(libs.elmslie.core)
    implementation(libs.elmslie.coroutines)
    implementation(libs.androidx.coreKtx)
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
    implementation(libs.androidx.appCompat)
    implementation(project(":core:utils"))
    implementation(project(":core:converter"))
    implementation(project(":design:res"))
}