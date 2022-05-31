package uk.co.zac_h.spacex.statistics.graphs.launchrate

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
import uk.co.zac_h.spacex.types.RocketType
import uk.co.zac_h.spacex.utils.formatDateMillisYYYY
import uk.co.zac_h.spacex.utils.models.RateStatsModel
import javax.inject.Inject

@HiltViewModel
class LaunchRateViewModel @Inject constructor(
    private val repository: StatisticsRepository
) : ViewModel() {

    private val _launchRate = MutableLiveData<ApiResult<List<RateStatsModel>>>()
    val launchRate: LiveData<ApiResult<List<RateStatsModel>>> = _launchRate

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun get(cachePolicy: CachePolicy = CachePolicy.EXPIRES) {
        viewModelScope.launch {
            val response = async(_launchRate) {
                repository.fetch(
                    key = "launch_rate",
                    query = StatisticsQuery.launchRateQuery,
                    cachePolicy = cachePolicy
                )
            }

            val result = response.await().map { launches -> launches.docs.map { Launch(it) } }

            _launchRate.value = result.map { launches ->
                ArrayList<RateStatsModel>().apply {
                    launches.forEach {
                        val year = it.launchDate?.dateUnix?.formatDateMillisYYYY() ?: return@forEach

                        if (none { newList -> newList.year == year }) add(RateStatsModel(year))

                        val stat = first { newList -> newList.year == year }

                        if (it.upcoming == false) it.success?.let { success ->
                            if (success) when (it.rocket?.type) {
                                RocketType.FALCON_ONE -> stat.falconOne++
                                RocketType.FALCON_NINE -> stat.falconNine++
                                RocketType.FALCON_HEAVY -> stat.falconHeavy++
                                else -> return@forEach
                            } else stat.failure++
                        } else stat.planned++
                    }
                }
            }
        }
    }

}