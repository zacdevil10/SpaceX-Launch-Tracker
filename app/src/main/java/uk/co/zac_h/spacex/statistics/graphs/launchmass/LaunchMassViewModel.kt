package uk.co.zac_h.spacex.statistics.graphs.launchmass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.async
import uk.co.zac_h.spacex.dto.spacex.*
import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.statistics.StatisticsRepository
import uk.co.zac_h.spacex.types.LaunchMassViewType
import uk.co.zac_h.spacex.types.RocketType
import uk.co.zac_h.spacex.utils.formatDateMillisYYYY
import uk.co.zac_h.spacex.utils.models.LaunchMassStatsModel
import uk.co.zac_h.spacex.utils.models.OrbitMassModel
import javax.inject.Inject

@HiltViewModel
class LaunchMassViewModel @Inject constructor(
    private val repository: StatisticsRepository
) : ViewModel() {

    private val _launchMass = MutableLiveData<ApiResult<List<LaunchMassStatsModel>>>()
    val launchMass: LiveData<ApiResult<List<LaunchMassStatsModel>>> = _launchMass

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    var filterRocket: RocketType? = null
        private set

    var filterType: LaunchMassViewType? = LaunchMassViewType.ROCKETS
        private set

    fun get(cachePolicy: CachePolicy = CachePolicy.EXPIRES) {
        viewModelScope.launch {
            val response = async(_launchMass) {
                repository.fetch(key = "launch_mass", query = query, cachePolicy = cachePolicy)
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
        }
    }

    private val query = QueryModel(
        QueryUpcomingSuccessLaunchesModel(upcoming = false, success = true),
        QueryOptionsModel(
            false,
            listOf(
                QueryPopulateModel(
                    "payloads",
                    populate = "",
                    select = listOf("mass_kg", "orbit")
                ),
                QueryPopulateModel("rocket", populate = "", select = listOf("id", "name"))
            ),
            QueryLaunchesSortByDate("asc"),
            listOf("payloads", "name", "date_unix", "rocket"),
            1000000
        )
    )

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

    fun setRocketFilter(value: RocketType?) {
        filterRocket = value
    }

    fun setTypeFilter(value: LaunchMassViewType?) {
        filterType = value
    }

}