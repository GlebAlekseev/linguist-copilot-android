@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.toString() == "com.github.takahirom.decomposer") {
                useModule("com.github.takahirom:decomposer:main-SNAPSHOT")
            }
        }
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
include(":app:api")
include(":design:res")
include(":design:compose_ui")
include(":core:converter")
include(":core:python")
include(":core:elm")
include(":core:di")
include(":core:crypto")
include(":core:utils")
include(":core:book")
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
include(":feature:bookReader:api")
include(":feature:bookReader:impl")
include(":feature:bookSearch:api")
include(":feature:bookSearch:impl")
include(":feature:book:api")
include(":feature:book:impl")