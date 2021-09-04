package uk.co.zac_h.spacex.statistics.graphs.landinghistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.dto.spacex.*
import uk.co.zac_h.spacex.statistics.StatisticsRepository
import uk.co.zac_h.spacex.utils.formatDateMillisYYYY
import uk.co.zac_h.spacex.utils.models.LandingHistoryModel
import javax.inject.Inject

@HiltViewModel
class LandingHistoryViewModel @Inject constructor(
    private val repository: StatisticsRepository
) : ViewModel() {

    private val _landingHistory =
        MutableLiveData<ApiResult<List<LandingHistoryModel>>>(ApiResult.pending())
    val landingHistory: LiveData<ApiResult<List<LandingHistoryModel>>> = _landingHistory

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun get(cachePolicy: CachePolicy = CachePolicy.EXPIRES) {
        viewModelScope.launch {
            _landingHistory.value = ApiResult.pending()
            val response = async {
                repository.fetch(key = "landing_history", query = query, cachePolicy = cachePolicy)
            }

            val result = response.await().map { launches -> launches.docs.map { Launch(it) } }

            _landingHistory.value = result.map { launches ->
                ArrayList<LandingHistoryModel>().apply {
                    launches.forEach {
                        val year = it.launchDate?.dateUnix?.formatDateMillisYYYY() ?: return@forEach

                        if (none { newList -> newList.year == year }) add(LandingHistoryModel(year))

                        val stat = first { newList -> newList.year == year }

                        it.cores?.forEach { core ->
                            when (core.landingSuccess) {
                                true -> when (core.landingType) {
                                    "Ocean" -> stat.ocean++
                                    "RTLS" -> stat.rtls++
                                    "ASDS" -> stat.asds++
                                }
                                false -> stat.failures++
                            }
                        }
                    }
                }
            }
        }
    }

    private val query = QueryModel(
        QueryLandingHistory(true),
        QueryOptionsModel(
            false,
            listOf(
                QueryPopulateModel("cores.core", listOf("id"), ""),
                QueryPopulateModel("cores.landpad", listOf("id"), "")
            ),
            QueryLaunchesSortByDate("asc"),
            listOf("cores", "date_local", "date_unix"),
            1000000
        )
    )

}