plugins {
    alias(libs.plugins.spacex.android.library)
    alias(libs.plugins.spacex.android.compose)
    alias(libs.plugins.spacex.hilt)
}

android {
    namespace = "uk.co.zac_h.spacex.core.common"
}

dependencies {
    implementation(libs.androidx.compose.paging)

    implementation(project(":core:ui"))
    implementation(project(":network"))
}
