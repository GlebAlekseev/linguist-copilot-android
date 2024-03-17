@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("linguistcopilot.android.library")
    id("com.chaquo.python")
    alias(libs.plugins.kotlin.serialization)

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
            install("pydantic<2")
            install("-r", "requirements.txt")
            install("https://github.com/explosion/spacy-models/releases/download/en_core_web_sm-2.2.0/en_core_web_sm-2.2.0.tar.gz")
        }
    }
}

dependencies {
    androidTestImplementation(libs.androidx.testExtJunit)
    testImplementation(libs.junit)
    implementation(libs.kotlinx.serialization.json)

}