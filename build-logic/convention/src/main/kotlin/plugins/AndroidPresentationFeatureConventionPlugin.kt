package plugins

import com.android.build.api.dsl.LibraryExtension
import config.Config
import extensions.configureAndroidKotlin
import extensions.configureBuildTypes
import extensions.configurePresentationFeature
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidPresentationFeatureConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project){
            with(pluginManager) {
                apply("org.jetbrains.kotlin.kapt")
            }
            extensions.configure<LibraryExtension> {
                configurePresentationFeature(this)
            }
        }
    }
}