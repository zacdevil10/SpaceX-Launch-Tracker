package uk.co.zac_h.spacex.feature.assets.vehicles

/**
 * The kinds of asset that open a detail pane, each with the Assets tab that lists them.
 */
enum class VehicleType(val page: Int) {
    ASTRONAUT(page = 0),
    ROCKET(page = 1),
    SECOND_STAGE(page = 2),
    CORE(page = 3),
    CAPSULE(page = 4);

    companion object {
        fun forPage(page: Int): VehicleType = entries.first { it.page == page }
    }
}
