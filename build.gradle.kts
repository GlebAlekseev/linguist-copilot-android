buildscript {
    dependencies {
        classpath(libs.agp)
    }
}

plugins {
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.gradle.android.cacheFix) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.chaquo) apply false
    id("com.github.takahirom.decomposer") version "main-SNAPSHOT"
}
