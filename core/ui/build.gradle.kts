plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "uk.co.zac_h.spacex.core.ui"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-proguard-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    lint {
        // The compose-runtime lint check from Compose BOM 2024.05 crashes under Kotlin 2.4's UAST
        // (NPE in ComposableCoroutineCreationDetector on TabRow.kt). Re-enable after the Compose BOM update.
        disable += "CoroutineCreationDuringComposition"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core)

    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.ui.toolingpreview)
    debugApi(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.compose.navigation)

    api(libs.coil.compose)
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}
