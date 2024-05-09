package uk.co.zac_h.spacex.core.common.types

import uk.co.zac_h.spacex.network.SPACEX_ACTIVE
import uk.co.zac_h.spacex.network.SPACEX_DECEASED
import uk.co.zac_h.spacex.network.SPACEX_INACTIVE
import uk.co.zac_h.spacex.network.SPACEX_IN_TRAINING
import uk.co.zac_h.spacex.network.SPACEX_LOST_IN_TRAINING
import uk.co.zac_h.spacex.network.SPACEX_RETIRED
import uk.co.zac_h.spacex.network.SPACEX_UNKNOWN

enum class CrewStatus(val status: String) {
    IN_TRAINING(SPACEX_IN_TRAINING),
    ACTIVE(SPACEX_ACTIVE),
    INACTIVE(SPACEX_INACTIVE),
    RETIRED(SPACEX_RETIRED),
    DECEASED(SPACEX_DECEASED),
    LOST_IN_TRAINING(SPACEX_LOST_IN_TRAINING),
    UNKNOWN(SPACEX_UNKNOWN)
}