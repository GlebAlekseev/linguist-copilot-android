plugins {
    id("linguistcopilot.feature.impl")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization.json)

    androidTestImplementation(libs.androidx.testExtJunit)
    testImplementation(libs.junit)

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
    implementation(project(":core:elm"))
    implementation(project(":core:di"))
    implementation(project(":design:res"))
    implementation(project(":feature:book:api"))
}