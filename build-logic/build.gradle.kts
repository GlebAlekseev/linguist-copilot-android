import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java-gradle-plugin")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.ksp.gradle.plugin)
    implementation(libs.agp)
    implementation(libs.detekt.gradle.plugin)
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
}

gradlePlugin {
    plugins.create("AndroidComposeConventionPlugin") {
        id = "linguistcopilot.android.compose"
        displayName = "Linguist Copilot Compose Android Library Plugin"
        description =
            "Gradle Plugin for setting up android library module with Compose dependencies"
        implementationClass = "plugins.AndroidComposeConventionPlugin"
    }
    plugins.create("KspConventionPlugin") {
        id = "linguistcopilot.ksp"
        displayName = "Kotlin Symbol Processing Support Plugin"
        description = "The Gradle Plugin that adds KSP support to the module"
        implementationClass = "plugins.KspConventionPlugin"
    }
    plugins.create("DetektConventionPlugin") {
        id = "linguistcopilot.detekt"
        displayName = "Detekt configuration Plugin"
        description = "The Gradle Plugin that configures Detekt in subproject"
        implementationClass = "plugins.DetektConventionPlugin"
    }
    plugins.create("FeatureApiConventionPlugin") {
        id = "linguistcopilot.feature.api"
        displayName = "Linguist Copilot Feature Api Convention"
        description = "The Gradle Plugin that configures Feature Api module"
        implementationClass = "plugins.FeatureApiConventionPlugin"
    }
    plugins.create("FeatureImplementationConventionPlugin") {
        id = "linguistcopilot.feature.impl"
        displayName = "Linguist Copilot Feature Implementation Convention"
        description = "The Gradle Plugin that configures Feature Implementation module"
        implementationClass = "plugins.FeatureImplementationConventionPlugin"
    }
    plugins.create("KotlinLibraryConventionPlugin") {
        id = "linguistcopilot.kotlin.library"
        displayName = "MpeiX Kotlin Library Convention"
        description = "The Gradle Plugin that configures Kotlin Library module"
        implementationClass = "plugins.KotlinLibraryConventionPlugin"
    }
    plugins.create("AndroidLibraryConventionPlugin") {
        id = "linguistcopilot.android.library"
        displayName = "Linguist Copilot Android Library Convention"
        description = "The Gradle Plugin that configures Android Library module"
        implementationClass = "plugins.AndroidLibraryConventionPlugin"
    }
    plugins.create("AndroidApplicationConventionPlugin") {
        id = "linguistcopilot.android.application"
        displayName = "Linguist Copilot Android Application Convention"
        description = "The Gradle Plugin that configures Android application"
        implementationClass = "plugins.AndroidApplicationConventionPlugin"
    }
    plugins.create("AndroidUiConventionPlugin") {
        id = "linguistcopilot.android.ui"
        displayName = "Linguist Copilot Android Ui Convention"
        description = "The Gradle Plugin that configures Android Library with UI Components module"
        implementationClass = "plugins.AndroidUiConventionPlugin"
    }
    plugins.create("AndroidResourcesConventionPlugin") {
        id = "linguistcopilot.android.resources"
        displayName = "Linguist Copilot Android Resources Convention"
        description = "The Gradle Plugin that configures Android module with resources only"
        implementationClass = "plugins.AndroidResourcesConventionPlugin"
    }
    plugins.create("KotlinJvmParcelizePlugin") {
        id = "linguistcopilot.kotlin.parcelize"
        displayName = "Parcelize Support Plugin"
        description = "The plugin allows to use `@Parcelize` annotation in non-android modules"
        implementationClass = "plugins.KotlinJvmParcelizePlugin"
    }
}
