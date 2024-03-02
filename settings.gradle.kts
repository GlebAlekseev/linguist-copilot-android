@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
    includeBuild("build-logic")
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "Linguist Copilot"

include(":app")
include(":core:utils")
include(":core:book")
include(":design:res")
include(":design:compose_ui")
include(":feature:onboarding:impl")
include(":feature:onboarding:api")
include(":feature:auth:api")
include(":feature:auth:impl")
include(":feature:content:api")
include(":feature:content:impl")
include(":feature:bookDownload:api")
include(":feature:bookDownload:impl")
include(":feature:bookDescription:api")
include(":feature:bookDescription:impl")
