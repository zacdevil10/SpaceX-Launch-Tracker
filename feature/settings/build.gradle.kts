plugins {
    alias(libs.plugins.spacex.android.library)
    alias(libs.plugins.spacex.android.compose)
    alias(libs.plugins.spacex.hilt)
}

android {
    namespace = "uk.co.zac_h.spacex.feature.settings"
}

dependencies {
    implementation(libs.androidx.compose.hilt.viewmodel)
    implementation(libs.androidx.compose.lifecycle.runtime)

    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":network"))

    implementation(libs.androidx.datastore.preferences)
}
