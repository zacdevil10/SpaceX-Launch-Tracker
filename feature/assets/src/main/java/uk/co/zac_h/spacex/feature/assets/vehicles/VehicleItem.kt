package uk.co.zac_h.spacex.feature.assets.vehicles

interface VehicleItem {

    val id: Any

    val title: String?

    val imageUrl: String?

    val description: String?
        get() = null

    val longDescription: String?
        get() = null
}
