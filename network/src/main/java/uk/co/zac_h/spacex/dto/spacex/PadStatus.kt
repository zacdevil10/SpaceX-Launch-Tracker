package uk.co.zac_h.spacex.dto.spacex

import uk.co.zac_h.spacex.*

enum class PadStatus(val status: String) {
    ACTIVE(SPACEX_ACTIVE),
    INACTIVE(SPACEX_INACTIVE),
    UNKNOWN(SPACEX_UNKNOWN),
    RETIRED(SPACEX_RETIRED),
    LOST(SPACEX_LOST),
    UNDER_CONSTRUCTION(SPACEX_UNDER_CONSTRUCTION)
}
