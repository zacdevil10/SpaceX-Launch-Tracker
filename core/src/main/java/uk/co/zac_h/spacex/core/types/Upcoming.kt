package uk.co.zac_h.spacex.core.types

enum class Upcoming(val text: String, val upcoming: Boolean) {
    NEXT("next", true), LATEST("latest", false)
}
