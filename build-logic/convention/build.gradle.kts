import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
     implementation(libs.android.gradle.plugin)
     implementation(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApp") {
            id = "conventionPluginsApp.android.application"
            implementationClass = "plugins.AndroidAppConventionPlugin"
        }
        register("androidLib") {
            id = "conventionPluginsApp.android.library"
            implementationClass = "plugins.AndroidLibConventionPlugin"
        }
        register("androidPresentationFeature") {
            id = "conventionPluginsApp.android.library.presentationFeature"
            implementationClass = "plugins.AndroidPresentationFeatureConventionPlugin"
        }
        register("androidCompose") {
            id = "conventionPluginsApp.android.library.compose"
            implementationClass = "plugins.AndroidComposeConventionPlugin"
        }
        register("androidComposeMetrics") {
            id = "conventionPluginsApp.android.library.compose.metrics"
            implementationClass = "plugins.AndroidComposeMetricsConventionPlugin"
        }
    }
}