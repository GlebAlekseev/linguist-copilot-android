package extensions

import com.android.build.api.dsl.CommonExtension
import config.Config
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile



internal fun Project.configurePresentationFeature(
    extension: CommonExtension<*, *, *, *, *>,
) {
    with(extension) {
        dependencies {
            add("implementation", versionCatalog().findLibrary("dagger").get())
            add("kapt", versionCatalog().findLibrary("dagger-compiler").get())
            add("implementation", versionCatalog().findLibrary("lifecycle-viewmodel").get())
            add("implementation", versionCatalog().findLibrary("lifecycle-runtime").get())
            add("implementation", versionCatalog().findLibrary("navigation-fragment").get())
        }
    }
}