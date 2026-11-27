// Repositories shared by every form factor. The only module besides :app that sees :network: it
// maps Launch Library DTOs to :core:model so no UI module depends on generated types.
plugins {
    alias(libs.plugins.spacex.android.library)
    alias(libs.plugins.spacex.hilt)
}

android {
    namespace = "uk.co.zac_h.spacex.core.data"
}

dependencies {
    api(project(":core:model"))
    implementation(project(":network"))

    api(libs.androidx.paging)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.serialization.json)
}
