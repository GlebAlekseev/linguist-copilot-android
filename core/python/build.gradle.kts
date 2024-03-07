@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("linguistcopilot.android.library")
    id("com.chaquo.python")

}
android {
    defaultConfig {
        ndk {
            abiFilters += listOf("arm64-v8a", "x86_64")
        }
    }
}
chaquopy{
    defaultConfig {
        buildPython("python3.8")
        version = "3.8"
        pip{
            install("-r", "requirements.txt")
        }
    }
}

dependencies {
    androidTestImplementation(libs.androidx.testExtJunit)
    testImplementation(libs.junit)

}