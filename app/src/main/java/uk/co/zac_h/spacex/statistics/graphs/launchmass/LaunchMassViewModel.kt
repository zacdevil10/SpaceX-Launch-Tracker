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
import uk.co.zac_h.spacex.statistics.StatisticsRepository
import uk.co.zac_h.spacex.utils.*
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

    var filterType: LaunchMassViewType? = LaunchMassViewType.ROCKETS

    var filterState: Boolean = false
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
                        val year = launch.launchDate?.dateUnix?.formatDateMillisYYYY() ?: return@forEach

                        if (none { it.year == year }) add(LaunchMassStatsModel(year))

                        val stat = first { it.year == year }

                        when (launch.rocket?.id) {
                            RocketIds.FALCON_ONE -> launch.payloads?.forEach { payload ->
                                updateOrbitMass(stat.falconOne, payload.orbit, payload.mass?.kg)
                            }
                            RocketIds.FALCON_NINE -> launch.payloads?.forEach { payload ->
                                updateOrbitMass(stat.falconNine, payload.orbit, payload.mass?.kg)
                            }
                            RocketIds.FALCON_HEAVY -> launch.payloads?.forEach { payload ->
                                updateOrbitMass(stat.falconHeavy, payload.orbit, payload.mass?.kg)
                            }
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
                QueryPopulateModel("rocket", populate = "", select = listOf("id"))
            ),
            QueryLaunchesSortByDate("asc"),
            listOf("payloads", "name", "date_unix", "rocket"),
            1000000
        )
    )

    fun showFilter(visibility: Boolean) {
        filterState = visibility
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

}