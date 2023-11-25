@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("conventionPluginsApp.android.application")
}

dependencies {
    implementation(project(":design"))
}