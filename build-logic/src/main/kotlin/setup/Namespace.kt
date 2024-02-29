package setup

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.configureNamespace(extension: CommonExtension<*, *, *, *, *>) {
    extension.namespace = "pro.linguistcopilot.$name"
}
