package uk.co.zac_h.spacex.statistics.graphs.launchmass

import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.RocketIds
import uk.co.zac_h.spacex.utils.add
import uk.co.zac_h.spacex.utils.formatDateMillisYYYY
import uk.co.zac_h.spacex.utils.models.KeysModel
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

    override fun populateOrbitKey(f1: OrbitMassModel?, f9: OrbitMassModel?, fh: OrbitMassModel?) {
        view.updateKey(ArrayList<KeysModel>().apply {
            0f.add(f1?.LEO, f9?.LEO, fh?.LEO).apply {
                if (this > 0) add(KeysModel("LEO", this))
            }
            0f.add(f1?.GTO, f9?.GTO, fh?.GTO).apply {
                if (this > 0) add(KeysModel("GTO", this))
            }
            0f.add(f1?.SSO, f9?.SSO, fh?.SSO).apply {
                if (this > 0) add(KeysModel("SSO", this))
            }
            0f.add(f1?.ISS, f9?.ISS, fh?.ISS).apply {
                if (this > 0) add(KeysModel("ISS", this))
            }
            0f.add(f1?.HCO, f9?.HCO, fh?.HCO).apply {
                if (this > 0) add(KeysModel("HCO", this))
            }
            0f.add(f1?.MEO, f9?.MEO, fh?.MEO).apply {
                if (this > 0) add(KeysModel("MEO", this))
            }
            0f.add(f1?.SO, f9?.SO, fh?.SO).apply {
                if (this > 0) add(KeysModel("SO", this))
            }
            0f.add(f1?.ED_L1, f9?.ED_L1, fh?.ED_L1).apply {
                if (this > 0) add(KeysModel("ED-L1", this))
            }
            0f.add(f1?.other, f9?.other, fh?.other).apply {
                if (this > 0) add(KeysModel("Other", this))
            }
            add(KeysModel("Total", 0f.add(f1?.total, f9?.total, fh?.total)))
        })
    }

    override fun populateRocketKey(f1: OrbitMassModel?, f9: OrbitMassModel?, fh: OrbitMassModel?) {
        view.updateKey(ArrayList<KeysModel>().apply {
            f1?.let {
                if (it.total > 0) add(KeysModel("Falcon 1", it.total))
            }
            f9?.let {
                if (it.total > 0) add(KeysModel("Falcon 9", it.total))
            }
            fh?.let {
                if (it.total > 0) add(KeysModel("Falcon Heavy", it.total))
            }
            add(KeysModel("Total", 0f.add(f1?.total, f9?.total, fh?.total)))
        })
    }

    override fun onSuccess(launches: List<Launch>?, animate: Boolean) {
        val massStats = ArrayList<LaunchMassStatsModel>()

        launches?.forEach { launch ->
            val year = launch.launchDate?.dateUnix?.formatDateMillisYYYY() ?: return@forEach

            if (massStats.none { it.year == year }) massStats.add(LaunchMassStatsModel(year))

            val stat = massStats.filter { it.year == year }[0]

            when (launch.rocket?.id) {
                RocketIds.FALCON_ONE -> launch.payloads?.forEach { payload ->
                    updateOrbitMass(
                        stat.falconOne,
                        payload.orbit,
                        payload.mass?.kg
                    )
                }
                RocketIds.FALCON_NINE -> launch.payloads?.forEach { payload ->
                    updateOrbitMass(
                        stat.falconNine,
                        payload.orbit,
                        payload.mass?.kg
                    )
                }
                RocketIds.FALCON_HEAVY -> launch.payloads?.forEach { payload ->
                    updateOrbitMass(
                        stat.falconHeavy,
                        payload.orbit,
                        payload.mass?.kg
                    )
                }
            }
        }

        view.apply {
            hideProgress()
            updateData(massStats, animate)
        }
    }

    private fun updateOrbitMass(model: OrbitMassModel, orbit: String?, mass: Float?) {
        mass?.let {
            model.total += mass
            when (orbit) {
                "LEO" -> model.LEO += mass
                "GTO" -> model.GTO += mass
                "GEO" -> model.GTO += mass
                "PO" -> model.LEO += mass
                "SSO" -> model.SSO += mass
                "ISS" -> model.ISS += mass
                "HCO" -> model.HCO += mass
                "MEO" -> model.MEO += mass
                "VLEO" -> model.LEO += mass
                "SO" -> model.SO += mass
                "ES-L1" -> model.ED_L1 += mass
                else -> model.other += mass
            }
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }

}