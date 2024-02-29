package plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import setup.configureKotlinAndroid
import setup.configureNamespace

@Suppress("unused")
internal class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply(Plugins.AndroidLibrary)
                apply(Plugins.KotlinAndroid)
                apply(Plugins.GradleAndroidCacheFix)
                apply(Plugins.LinguistCopilotDetekt)
            }

            extensions.configure<LibraryExtension> {
                configureNamespace(this)
                configureKotlinAndroid(this)

                @Suppress("UnstableApiUsage")
                with(buildFeatures) {
                    compose = false
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
