package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.model.spacex.CapsulesModel

interface CapsulesView {

    fun updateCapsules(capsules: List<CapsulesModel>)

    fun showProgress()

    fun hideProgress()

    fun toggleSwipeRefresh(refreshing: Boolean)

    fun showError(error: String)
}