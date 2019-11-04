package uk.co.zac_h.spacex.vehicles.cores

import uk.co.zac_h.spacex.model.CoreModel

interface CoreView {

    fun updateCores(cores: List<CoreModel>)

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)
}