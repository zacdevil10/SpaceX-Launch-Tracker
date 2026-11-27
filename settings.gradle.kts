pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SpaceX"
include(":app")
include(":feature:launch")
include(":feature:news")
include(":feature:assets")
include(":feature:settings")
include(":core:ui")
include(":core:common")
include(":core:database")
include(":network")
