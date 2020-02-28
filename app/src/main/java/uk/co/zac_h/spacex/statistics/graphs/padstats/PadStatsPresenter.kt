package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.rest.SpaceXInterface

interface PadStatsPresenter {

    fun getPads(api: SpaceXInterface = SpaceXInterface.create())

    fun cancelRequests()

}