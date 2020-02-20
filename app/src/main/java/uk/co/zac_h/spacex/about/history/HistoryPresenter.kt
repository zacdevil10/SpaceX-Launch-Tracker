package uk.co.zac_h.spacex.about.history

import uk.co.zac_h.spacex.rest.SpaceXInterface

interface HistoryPresenter {

    fun getHistory(api: SpaceXInterface = SpaceXInterface.create())

    fun cancelRequest()
}