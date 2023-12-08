@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("conventionPluginsApp.android.library")
}

android {
    namespace = "me.ag2s"
}

dependencies {
    implementation("androidx.annotation:annotation:1.7.0")

}