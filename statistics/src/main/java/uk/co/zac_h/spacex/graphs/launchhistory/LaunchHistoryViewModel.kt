package uk.co.zac_h.spacex.graphs.launchhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.*
import uk.co.zac_h.spacex.dto.spacex.*
import uk.co.zac_h.spacex.models.HistoryStatsModel
import uk.co.zac_h.spacex.utils.LaunchHistoryFilter
import uk.co.zac_h.spacex.utils.RocketIds
import uk.co.zac_h.spacex.utils.RocketType
import javax.inject.Inject

@HiltViewModel
class LaunchHistoryViewModel @Inject constructor(
    private val repository: StatisticsRepository
) : ViewModel() {

    private val _launchHistory = MutableLiveData<ApiResult<List<HistoryStatsModel>>>()
    val launchHistory: LiveData<ApiResult<List<HistoryStatsModel>>> = _launchHistory

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    var filterValue: LaunchHistoryFilter? = null
        private set

    var filterState: Boolean = false
        private set

    fun get(cachePolicy: CachePolicy = CachePolicy.EXPIRES) {
        viewModelScope.launch {
            val response = async(_launchHistory) {
                repository.fetch(key = "launch_history", query = query, cachePolicy = cachePolicy)
            }

            val result = response.await().map { launches -> launches.docs.map { Launch(it) } }

            _launchHistory.value = result.map { launches ->
                val falconOne = HistoryStatsModel(RocketType.FALCON_ONE)
                val falconNine = HistoryStatsModel(RocketType.FALCON_NINE)
                val falconHeavy = HistoryStatsModel(RocketType.FALCON_HEAVY)

                launches.forEach {
                    when (it.rocket?.id) {
                        RocketIds.FALCON_ONE -> {
                            it.success?.let { success ->
                                if (success) falconOne.successes++ else falconOne.failures++
                            }
                            falconOne.successRate = it.rocket?.successRate ?: 0
                        }
                        RocketIds.FALCON_NINE -> {
                            it.success?.let { success ->
                                if (success) falconNine.successes++ else falconNine.failures++
                            }
                            falconNine.successRate = it.rocket?.successRate ?: 0
                        }
                        RocketIds.FALCON_HEAVY -> {
                            it.success?.let { success ->
                                if (success) falconHeavy.successes++ else falconHeavy.failures++
                            }
                            falconHeavy.successRate = it.rocket?.successRate ?: 0
                        }
                    }
                }

                listOf(falconOne, falconNine, falconHeavy)
            }
        }
    }

    private val query = QueryModel(
        QueryUpcomingLaunchesModel(false),
        QueryOptionsModel(
            false, listOf(
                QueryPopulateModel("rocket", populate = "", select = listOf("success_rate_pct"))
            ), "", listOf("rocket", "success"), 100000
        )
    )

    fun setFilter(filter: LaunchHistoryFilter?) {
        filterValue = filter
    }

    fun showFilter(visibility: Boolean) {
        filterState = visibility
    }

}