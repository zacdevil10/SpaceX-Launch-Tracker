import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "uk.co.zac_h.spacex.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradleApi)
    compileOnly(libs.kotlin.gradlePluginApi)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.spacex.android.application.get().pluginId
            implementationClass = "uk.co.zac_h.spacex.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.spacex.android.library.get().pluginId
            implementationClass = "uk.co.zac_h.spacex.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = libs.plugins.spacex.android.compose.get().pluginId
            implementationClass = "uk.co.zac_h.spacex.buildlogic.AndroidComposeConventionPlugin"
        }
        register("hilt") {
            id = libs.plugins.spacex.hilt.get().pluginId
            implementationClass = "uk.co.zac_h.spacex.buildlogic.HiltConventionPlugin"
        }
    }
}
