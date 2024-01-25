package extensions

import com.android.build.api.dsl.CommonExtension
import config.Config
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureAndroidKotlin(
    extension: CommonExtension<*, *, *, *, *>,
) {
    with(extension) {
        namespace = Config.android.nameSpace
        compileSdk = Config.android.compileSdkVersion

        compileOptions {
            sourceCompatibility = Config.jvm.javaVersion
            targetCompatibility = Config.jvm.javaVersion
        }
        packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"

        buildFeatures.viewBinding = true

        packaging {
            resources.excludes.add("META-INF/*")
        }

        dependencies {
            add("implementation", versionCatalog().findLibrary("splitties-appctx").get())
            add("implementation", versionCatalog().findLibrary("core-ktx").get())
            add("implementation", versionCatalog().findLibrary("appcompat").get())
            add("implementation", versionCatalog().findLibrary("androidx-lifecycle-runtime-ktx").get())
            add("testImplementation", versionCatalog().findLibrary("junit").get())
            add("androidTestImplementation", versionCatalog().findLibrary("androidx-test-ext-junit").get())
            add("androidTestImplementation", versionCatalog().findLibrary("espresso-core").get())
        }
        defaultConfig.apply {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables.useSupportLibrary = true
            minSdk = Config.android.minSdkVersion
        }
    }
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = Config.jvm.kotlinJvm
            freeCompilerArgs = freeCompilerArgs + Config.jvm.freeCompilerArgs
        }
    }
}