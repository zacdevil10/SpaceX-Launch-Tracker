package uk.co.zac_h.spacex.statistics.graphs.launchmass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.core.common.types.LaunchMassViewType
import uk.co.zac_h.spacex.core.common.types.RocketType
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.statistics.StatisticsRepository
import uk.co.zac_h.spacex.utils.models.LaunchMassStatsModel
import uk.co.zac_h.spacex.utils.models.OrbitMassModel
import javax.inject.Inject

@HiltViewModel
class LaunchMassViewModel @Inject constructor(
    private val repository: StatisticsRepository
) : ViewModel() {

    private val _launchMass = MutableLiveData<ApiResult<List<LaunchMassStatsModel>>>()
    val launchMass: LiveData<ApiResult<List<LaunchMassStatsModel>>> = _launchMass

    /*val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation*/

    var filterRocket: RocketType? = null
        private set

    var filterType: LaunchMassViewType? = LaunchMassViewType.ROCKETS
        private set

    fun get(cachePolicy: CachePolicy = CachePolicy.EXPIRES) {
        /*viewModelScope.launch {
            val response = async(_launchMass) {
                repository.fetch(
                    key = "launch_mass",
                    query = StatisticsQuery.launchMassQuery,
                    cachePolicy = cachePolicy
                )
            }

            val result = response.await().map { launches -> launches.docs.map { Launch(it) } }

            _launchMass.value = result.map { launches ->
                ArrayList<LaunchMassStatsModel>().apply {
                    launches.forEach { launch ->
                        val year =
                            launch.launchDate?.dateUnix?.formatDateMillisYYYY() ?: return@forEach

                        if (none { it.year == year }) add(LaunchMassStatsModel(year))

                        val stat = first { it.year == year }

                        when (launch.rocket?.type) {
                            RocketType.FALCON_ONE -> launch.payloads?.forEach { payload ->
                                updateOrbitMass(stat.falconOne, payload.orbit, payload.mass?.kg)
                            }
                            RocketType.FALCON_NINE -> launch.payloads?.forEach { payload ->
                                updateOrbitMass(stat.falconNine, payload.orbit, payload.mass?.kg)
                            }
                            RocketType.FALCON_HEAVY -> launch.payloads?.forEach { payload ->
                                updateOrbitMass(stat.falconHeavy, payload.orbit, payload.mass?.kg)
                            }
                            RocketType.STARSHIP -> launch.payloads?.forEach { payload ->
                                updateOrbitMass(stat.starship, payload.orbit, payload.mass?.kg)
                            }
                            else -> return@forEach
                        }
                    }
                }
            }
        }*/
    }

    private fun updateOrbitMass(model: OrbitMassModel, orbit: String?, mass: Float?) {
        mass?.let {
            model.total += mass
            when (orbit) {
                "LEO" -> model.leo += mass
                "GTO" -> model.gto += mass
                "GEO" -> model.gto += mass
                "PO" -> model.leo += mass
                "SSO" -> model.sso += mass
                "ISS" -> model.iss += mass
                "HCO" -> model.hco += mass
                "MEO" -> model.meo += mass
                "VLEO" -> model.leo += mass
                "SO" -> model.so += mass
                "ES-L1" -> model.edL1 += mass
                else -> model.other += mass
            }
        }
    }

    fun setRocketFilter(value: RocketType?) {
        filterRocket = value
    }

    fun setTypeFilter(value: LaunchMassViewType?) {
        filterType = value
    }
}
