dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.android.application") {
                useVersion("8.4.1")
            }
            if (requested.id.id == "org.jetbrains.kotlin.android") {
                useVersion("1.9.24")
            }
            if (requested.id.id == "com.google.dagger.hilt.android") {
                useVersion("2.51.1")
            }
            if (requested.id.id == "com.android.tools.build:gradle") {
                useVersion("8.4.1")
            }
        }
    }
}

rootProject.name = "NewPokedex"
include(":app")
