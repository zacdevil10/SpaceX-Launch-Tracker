plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
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
        versionCode = 210010702
        versionName = "1.7.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        named("release") {
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
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
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
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.compose.materialWindow)

    //Feature
    implementation(project(":feature:launch"))
    implementation(project(":feature:news"))
    implementation(project(":feature:assets"))
    implementation(project(":feature:settings"))

    //Core
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":network"))

    //Navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    //MP Chart library
    implementation(libs.charts)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)

    //MVVM
    implementation(libs.androidx.livedata)
    implementation(libs.androidx.fragment)

    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.compose.navigation)
}
