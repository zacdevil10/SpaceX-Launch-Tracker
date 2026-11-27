plugins {
    alias(libs.plugins.spacex.android.library)
    alias(libs.plugins.spacex.android.compose)
    alias(libs.plugins.spacex.hilt)
}

android {
    namespace = "uk.co.zac_h.spacex.feature.starlink"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":network"))

    implementation(libs.androidx.viewmodel)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.compose.lifecycle.runtime)
    implementation(libs.androidx.compose.hilt.viewmodel)

    testImplementation(libs.junit)
}
