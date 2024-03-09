package plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import setup.configureAndroidCompose
import setup.configureKotlinAndroid
import setup.configureNamespace

@Suppress("unused")
class FeatureImplementationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply(Plugins.AndroidLibrary)
                apply(Plugins.KotlinAndroid)
                apply(Plugins.KotlinParcelize)
                apply(Plugins.GradleAndroidCacheFix)
                apply(Plugins.LinguistCopilotDetekt)
                apply(Plugins.LinguistCopilotKsp)
                apply(Plugins.TakahiromDecomposer)
            }

            extensions.configure<LibraryExtension> {
                configureNamespace(this)
                configureKotlinAndroid(this)
                configureAndroidCompose(this)

                @Suppress("UnstableApiUsage")
                with(buildFeatures) {
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
