package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.model.CapsulesModel

interface CapsulesView {

    fun updateCapsules(capsules: List<CapsulesModel>)

    fun showProgress()

    fun hideProgress()

    fun error(error: String)
}