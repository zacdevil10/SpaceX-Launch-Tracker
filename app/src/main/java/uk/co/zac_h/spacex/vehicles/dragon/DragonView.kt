package uk.co.zac_h.spacex.vehicles.dragon

import uk.co.zac_h.spacex.model.spacex.DragonModel

interface DragonView {

    fun updateDragon(dragon: List<DragonModel>)

    fun showProgress()

    fun hideProgress()

    fun toggleSwipeRefresh(refreshing: Boolean)

    fun showError(error: String)

}