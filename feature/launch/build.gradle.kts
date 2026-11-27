plugins {
    alias(libs.plugins.spacex.android.library)
    alias(libs.plugins.spacex.android.compose)
    alias(libs.plugins.spacex.hilt)
}

android {
    namespace = "uk.co.zac_h.spacex.feature.launch"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core)

    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":network"))

    implementation(libs.androidx.viewmodel)
    implementation(libs.androidx.compose.lifecycle.viewmodel)

    implementation(libs.androidx.compose.hilt.viewmodel)

    implementation(libs.androidx.paging)
    implementation(libs.androidx.compose.paging)

    implementation(libs.androidx.compose.lifecycle.runtime)
    implementation(libs.androidx.compose.runtime.livedata)
}
