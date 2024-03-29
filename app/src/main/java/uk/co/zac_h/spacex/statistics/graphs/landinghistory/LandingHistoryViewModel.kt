package uk.co.zac_h.spacex.statistics.graphs.landinghistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.statistics.StatisticsRepository
import uk.co.zac_h.spacex.utils.models.LandingHistoryModel
import javax.inject.Inject

@HiltViewModel
class LandingHistoryViewModel @Inject constructor(
    private val repository: StatisticsRepository
) : ViewModel() {

    private val _landingHistory = MutableLiveData<ApiResult<List<LandingHistoryModel>>>()
    val landingHistory: LiveData<ApiResult<List<LandingHistoryModel>>> = _landingHistory

    /*val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation*/

    fun get(cachePolicy: CachePolicy = CachePolicy.EXPIRES) {
        /*viewModelScope.launch {
            val response = async(_landingHistory) {
                repository.fetch(
                    key = "landing_history",
                    query = StatisticsQuery.landingHistoryQuery,
                    cachePolicy = cachePolicy
                )
            }

            val result = response.await().map { launches -> launches.docs.map { Launch(it) } }

            _landingHistory.value = result.map { launches ->
                ArrayList<LandingHistoryModel>().apply {
                    launches.forEach {
                        val year = it.launchDate?.dateUnix?.formatDateMillisYYYY() ?: return@forEach

                        if (none { newList -> newList.year == year }) add(LandingHistoryModel(year))

                        val stat = first { newList -> newList.year == year }

                        it.cores?.forEach { core ->
                            if (core.landingSuccess == true) when (core.landingType) {
                                "Ocean" -> stat.ocean++
                                "RTLS" -> stat.rtls++
                                "ASDS" -> stat.asds++
                            } else stat.failures++
                        }
                    }
                }
            }
        }*/
    }
}
