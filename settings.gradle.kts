pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    includeBuild("build-logic")
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Linguist Copilot"

include(":app")
include(":features:splash")
include(":design")
include(":core:feature-toggles")
include(":core:utils")
include(":navigation")
include(":features:main")
