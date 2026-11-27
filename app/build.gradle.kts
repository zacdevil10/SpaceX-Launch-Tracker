plugins {
    alias(libs.plugins.spacex.android.application)
    alias(libs.plugins.spacex.android.compose)
    alias(libs.plugins.spacex.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "uk.co.zac_h.spacex"

    defaultConfig {
        applicationId = "uk.co.zac_h.spacex"
        versionCode = 260020000
        versionName = "2.0.0"
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

    flavorDimensions.add("firebaseEnv")

    productFlavors {
        create("prod") {
            dimension = "firebaseEnv"
        }
        create("dev") {
            dimension = "firebaseEnv"
        }
    }
}

dependencies {

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

    // Navigation 3
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigationevent.compose)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.compose.material3.adaptive.navigation3)

    testImplementation(libs.junit)

    //Debug tools menu (debug builds only)
    debugImplementation(libs.androidx.compose.hilt.viewmodel)
    debugImplementation(libs.androidx.compose.lifecycle.runtime)
}
