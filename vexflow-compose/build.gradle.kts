plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
}

group = "nl.jacobras"
version = "0.0.1"

android {
    compileSdk = 34
    namespace = "nl.jacobras.vexflowcompose"

    buildFeatures {
        compose = true
    }
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget {
        publishLibraryVariants("release")
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(compose.uiTooling)
            }
        }
    }

    jvmToolchain(17)
}

// From https://github.com/gradle/gradle/issues/26091#issuecomment-1722947958
tasks.withType<AbstractPublishToMaven>().configureEach {
    val signingTasks = tasks.withType<Sign>()
    mustRunAfter(signingTasks)
}