package uk.co.zac_h.spacex.statistics.graphs.launchhistory

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
import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.query.StatisticsQuery
import uk.co.zac_h.spacex.statistics.StatisticsRepository
import uk.co.zac_h.spacex.types.LaunchHistoryFilter
import uk.co.zac_h.spacex.types.RocketType
import uk.co.zac_h.spacex.utils.models.HistoryStatsModel
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

    fun get(cachePolicy: CachePolicy = CachePolicy.EXPIRES) {
        viewModelScope.launch {
            val response = async(_launchHistory) {
                repository.fetch(
                    key = "launch_history",
                    query = StatisticsQuery.launchHistoryQuery,
                    cachePolicy = cachePolicy
                )
            }

            val result = response.await().map { launches -> launches.docs.map { Launch(it) } }

            _launchHistory.value = result.map { launches ->
                val falconOne = HistoryStatsModel(RocketType.FALCON_ONE)
                val falconNine = HistoryStatsModel(RocketType.FALCON_NINE)
                val falconHeavy = HistoryStatsModel(RocketType.FALCON_HEAVY)
                val starship = HistoryStatsModel(RocketType.STARSHIP)

                launches.forEach {
                    when (it.rocket?.type) {
                        RocketType.FALCON_ONE -> {
                            it.success?.let { success ->
                                if (success) falconOne.successes++ else falconOne.failures++
                            }
                            falconOne.successRate = it.rocket.successRate ?: 0
                        }
                        RocketType.FALCON_NINE -> {
                            it.success?.let { success ->
                                if (success) falconNine.successes++ else falconNine.failures++
                            }
                            falconNine.successRate = it.rocket.successRate ?: 0
                        }
                        RocketType.FALCON_HEAVY -> {
                            it.success?.let { success ->
                                if (success) falconHeavy.successes++ else falconHeavy.failures++
                            }
                            falconHeavy.successRate = it.rocket.successRate ?: 0
                        }
                        RocketType.STARSHIP -> {
                            it.success?.let { success ->
                                if (success) starship.successes++ else falconHeavy.failures++
                            }
                            starship.successRate = it.rocket.successRate ?: 0
                        }
                        else -> return@forEach
                    }
                }

                listOf(falconOne, falconNine, falconHeavy, starship)
            }
        }
    }

    fun setFilter(filter: LaunchHistoryFilter?) {
        filterValue = filter
    }

}