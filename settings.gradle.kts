pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "VexFlow Compose"
include(":sample-app")
include(":sample-desktop")
include(":vexflow-compose")
