package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import setup.configureKotlinJvm

@Suppress("unused")
class KotlinLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply(Plugins.KotlinJvm)
                apply(Plugins.LinguistCopilotDetekt)
            }
            configureKotlinJvm()
        }
    }
}
