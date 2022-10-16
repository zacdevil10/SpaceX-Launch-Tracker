package uk.co.zac_h.spacex.network.dto.spacex

import uk.co.zac_h.spacex.network.*

enum class PadStatus(val status: String) {
    ACTIVE(SPACEX_ACTIVE),
    INACTIVE(SPACEX_INACTIVE),
    UNKNOWN(SPACEX_UNKNOWN),
    RETIRED(SPACEX_RETIRED),
    LOST(SPACEX_LOST),
    UNDER_CONSTRUCTION(SPACEX_UNDER_CONSTRUCTION)
}
