plugins {
    alias(libs.plugins.spacex.android.library)
    alias(libs.plugins.spacex.android.compose)
    alias(libs.plugins.spacex.hilt)
}

android {
    namespace = "uk.co.zac_h.spacex.feature.assets"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    // Only for ApiResult and withRefresh until they move to :core in a later Phase 0 PR.
    implementation(project(":network"))

    implementation(libs.androidx.compose.runtime.livedata)

    //MVVM
    implementation(libs.androidx.viewmodel)

    implementation(libs.androidx.compose.lifecycle.runtime)
    implementation(libs.androidx.compose.hilt.viewmodel)
    implementation(libs.androidx.compose.paging)
}
