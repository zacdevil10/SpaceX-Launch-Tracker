plugins {
    alias(libs.plugins.spacex.android.library)
    alias(libs.plugins.spacex.android.compose)
}

android {
    namespace = "uk.co.zac_h.spacex.core.ui"
}

dependencies {
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.icons.core)
    api(libs.androidx.compose.ui.toolingpreview)
    debugApi(libs.androidx.compose.ui.tooling)

    api(libs.coil.compose)
    //Coil 3 needs a network artifact to load http(s) URLs. Pin its transitive OkHttp 4.x to 5.x via the BOM.
    implementation(libs.coil.network.okhttp)
    implementation(platform(libs.okhttp.bom))
}
