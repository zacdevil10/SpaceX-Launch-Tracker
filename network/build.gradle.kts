plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-proguard-rules.pro")
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
            buildConfigField("String", "URL", "\"https://ll.thespacedevs.com/2.2.0/\"")
        }
        named("debug") {
            buildConfigField("String", "URL", "\"https://lldev.thespacedevs.com/2.2.0/\"")
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
        buildConfig = true
    }
    namespace = "uk.co.zac_h.spacex.network"
}

dependencies {
    //Retrofit
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.converter.moshi)

    //Moshi
    ksp(libs.squareup.moshi)

    //Retrofit logging
    implementation(libs.logging.interceptor)

    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    //Paging
    implementation(libs.androidx.paging)
}
