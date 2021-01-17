package uk.co.zac_h.spacex.crew

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Crew

interface CrewView : NetworkInterface.View<List<Crew>> {
    fun startTransition() {}
}