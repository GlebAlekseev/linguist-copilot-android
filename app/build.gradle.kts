@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("conventionPluginsApp.android.application")
    alias(libs.plugins.kotlin.kapt)
}

dependencies {
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
    implementation(libs.navigation.fragment)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewmodel)
    implementation(project(":core:feature-toggles"))
    implementation(project(":core:utils"))
    implementation(project(":design"))
    implementation(project(":navigation"))
    implementation(project(":features:splash"))
    implementation(project(":features:main"))
    implementation(project(":features:reader"))
}
