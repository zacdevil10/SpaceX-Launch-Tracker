plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "uk.co.zac_h.spacex.feature.launch"
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
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)

    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":network"))

    implementation(libs.androidx.viewmodel)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.compose.lifecycle.viewmodel)

    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    implementation(libs.androidx.compose.hilt.navigation)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.paging)
    implementation(libs.androidx.compose.paging)

    implementation(libs.androidx.compose.lifecycle.runtime)
    implementation(libs.androidx.compose.runtime.livedata)
}