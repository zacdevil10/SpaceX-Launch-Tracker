plugins {
    alias(libs.plugins.spacex.android.library)
    alias(libs.plugins.spacex.hilt)
}

android {
    namespace = "uk.co.zac_h.spacex.core.database"
}

ksp {
    arg("room.generateKotlin", "true")
}

dependencies {
    api(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    api(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)
}
