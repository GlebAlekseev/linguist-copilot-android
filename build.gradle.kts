buildscript {
    dependencies {
        // specify android gradle plugin version in versions catalog
        classpath(libs.agp)
    }
}

plugins {
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.gradle.android.cacheFix) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt) apply false
}
