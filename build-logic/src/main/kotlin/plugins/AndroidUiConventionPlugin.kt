package plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import setup.configureAndroidCompose
import setup.configureKotlinAndroid
import setup.configureNamespace

@Suppress("unused")
class AndroidUiConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply(Plugins.AndroidLibrary)
                apply(Plugins.KotlinAndroid)
                apply(Plugins.LinguistCopilotDetekt)
                apply(Plugins.TakahiromDecomposer)
            }

            extensions.configure<LibraryExtension> {
                configureNamespace(this)
                configureKotlinAndroid(this)
                configureAndroidCompose(this)

                buildFeatures {
                    compose = true
                    viewBinding = false
                    androidResources = false
                    shaders = false
                    resValues = false
                    buildConfig = false
                }
            }
        }
    }
}
