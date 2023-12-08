@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("conventionPluginsApp.android.library")
    id("conventionPluginsApp.android.library.presentationFeature")
    id("kotlin-parcelize")
}

android {
    namespace = "pro.linguistcopilot.features.reader"

//    packaging {
//        resources.excludes.add("META-INF/*")
//    }
}

dependencies {
    implementation(libs.material)
    implementation(libs.gson.converter)
    implementation(project(":navigation"))
    implementation(project(":core:utils"))
    implementation(project(":design"))
    implementation(project(":features:reader:domain"))
    implementation(project(":features:reader:book"))

    implementation("org.jsoup:jsoup:1.17.1")
    implementation("cn.hutool:hutool-crypto:5.8.22")


    implementation("com.louiscad.splitties:splitties-appctx:3.0.0")
    implementation("com.louiscad.splitties:splitties-systemservices:3.0.0")
    implementation("com.louiscad.splitties:splitties-views:3.0.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.jetbrains.kotlin:kotlin-parcelize-runtime:1.9.10")

}