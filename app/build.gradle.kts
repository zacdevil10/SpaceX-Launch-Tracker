plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "uk.co.zac_h.spacex"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 260020000
        versionName = "2.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    flavorDimensions.add("firebaseEnv")

    productFlavors {
        create("prod") {
            dimension = "firebaseEnv"
        }
        create("dev") {
            dimension = "firebaseEnv"
        }
    }
    namespace = "uk.co.zac_h.spacex"
}

dependencies {
    implementation(libs.material)

    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.androidx.core)

    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.compose.materialWindow)

    implementation(project(":feature:launch"))
    implementation(project(":feature:news"))
    implementation(project(":feature:assets"))
    implementation(project(":feature:settings"))

    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":network"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.compose.navigation)
}
