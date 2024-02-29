package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import setup.configureAndroidCompose

@Suppress("unused")
class AndroidComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            extensions.configure<com.android.build.gradle.LibraryExtension> {
                configureAndroidCompose(this)

                @Suppress("UnstableApiUsage")
                buildFeatures {
                    compose = true
                }
            }
        }
    }
}
