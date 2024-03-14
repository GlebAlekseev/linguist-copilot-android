plugins {
    id("linguistcopilot.feature.api")
}

dependencies {
    implementation(libs.dagger)
    implementation(libs.kotlinx.coroutinesCoreJvm)
    api(project(":core:utils"))
    implementation(project(":feature:book:api"))
}