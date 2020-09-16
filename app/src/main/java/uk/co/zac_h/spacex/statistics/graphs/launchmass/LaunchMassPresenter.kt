package uk.co.zac_h.spacex.statistics.graphs.launchmass

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedDocsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.RocketIds
import uk.co.zac_h.spacex.utils.models.LaunchMassStatsModel
import uk.co.zac_h.spacex.utils.models.OrbitMassModel

class LaunchMassPresenter(
    private val view: LaunchMassContract.View,
    private val interactor: LaunchMassContract.Interactor
) : LaunchMassContract.Presenter, LaunchMassContract.Callback {

    override fun getLaunchList(api: SpaceXInterface) {
        view.showProgress()
        interactor.getLaunches(api, this)
    }

    override fun addLaunchList(statsList: ArrayList<LaunchMassStatsModel>) {
        view.hideProgress()
        view.updateData(statsList, false)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun showFilter(filterVisible: Boolean) {
        view.showFilter(filterVisible)
    }

    override fun updateFilter(statsList: ArrayList<LaunchMassStatsModel>) {
        view.updateData(statsList, false)
    }

    override fun onSuccess(launchDocs: LaunchesExtendedDocsModel?, animate: Boolean) {
        launchDocs?.docs?.let { launches ->
            val massStats = ArrayList<LaunchMassStatsModel>()

            println(launches)

            var year = 2005
            launches.forEach {
                val newYear =
                    it.launchDateLocal?.substring(0, 4).toString().toIntOrNull() ?: return@forEach

                if (newYear > year) {
                    if (newYear != year++) {
                        for (y in year until newYear) massStats.add(LaunchMassStatsModel(y))
                    }
                    massStats.add(LaunchMassStatsModel(newYear))
                    year = newYear
                }

                when (it.rocket?.id) {
                    RocketIds.FALCON_ONE -> it.payloads?.forEach { payload ->
                        updateOrbitMass(
                            massStats[massStats.lastIndex].falconOne,
                            payload.orbit,
                            payload.massKg
                        )
                    }
                    RocketIds.FALCON_NINE -> it.payloads?.forEach { payload ->
                        updateOrbitMass(
                            massStats[massStats.lastIndex].falconNine,
                            payload.orbit,
                            payload.massKg
                        )
                    }
                    RocketIds.FALCON_HEAVY -> it.payloads?.forEach { payload ->
                        updateOrbitMass(
                            massStats[massStats.lastIndex].falconHeavy,
                            payload.orbit,
                            payload.massKg
                        )
                    }
                }
            }


            view.apply {
                hideProgress()
                updateData(massStats, animate)
            }
        }
    }

    private fun updateOrbitMass(model: OrbitMassModel, orbit: String?, mass: Float?) {
        mass?.let {
            model.total += mass
            when (orbit) {
                "LEO" -> model.LEO += mass
                "GTO" -> model.GTO += mass
                "GEO" -> model.GTO += mass
                "PO" -> model.PO += mass
                "SSO" -> model.SSO += mass
                "ISS" -> model.ISS += mass
                "HCO" -> model.HCO += mass
                "MEO" -> model.MEO += mass
                "VLEO" -> model.LEO += mass
                "SO" -> model.SO += mass
                "ES-L1" -> model.ED_L1 += mass
            }
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }

}