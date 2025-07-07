pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "Wishlists"
include(":app")
include(":core:core-data")
include(":core:core-domain")
include(":core:core-ui")
include(":features:profile:profile-api")
include(":features:profile:profile-ui")
include(":features:profile:profile-domain")
include(":core:core-utils")
