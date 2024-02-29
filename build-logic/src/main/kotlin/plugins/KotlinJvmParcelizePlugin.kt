package plugins

import extension.implementation
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.plugin.JetBrainsSubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation


@Suppress("unused")
class KotlinJvmParcelizePlugin : KotlinCompilerPluginSupportPlugin {

    @Suppress("MaxLineLength")
    override fun apply(target: Project) {
        with(target) {
            val kotlinPluginVersion = getKotlinPluginVersion()
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-parcelize-runtime:$kotlinPluginVersion")
            }
        }
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean =
        kotlinCompilation is KotlinJvmCompilation

    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>,
    ): Provider<List<SubpluginOption>> {
        return kotlinCompilation.target.project.provider { emptyList() }
    }

    override fun getCompilerPluginId() = "org.jetbrains.kotlin.parcelize"

    override fun getPluginArtifact(): SubpluginArtifact =
        JetBrainsSubpluginArtifact(artifactId = "kotlin-parcelize-compiler")
}
