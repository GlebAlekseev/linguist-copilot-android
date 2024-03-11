plugins {
    id("linguistcopilot.feature.api")
}

dependencies {
    implementation(libs.dagger)
    implementation(libs.kotlinx.coroutinesCore)
    api(project(":core:utils"))
    implementation(project(":feature:book:api"))
}