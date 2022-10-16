package uk.co.zac_h.spacex.statistics.graphs.fairingrecovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.utils.formatDateMillisYYYY
import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.Repository
import uk.co.zac_h.spacex.network.async
import uk.co.zac_h.spacex.network.query.StatisticsQuery
import uk.co.zac_h.spacex.statistics.StatisticsRepository
import uk.co.zac_h.spacex.utils.models.FairingRecoveryModel
import javax.inject.Inject

@HiltViewModel
class FairingRecoveryViewModel @Inject constructor(
    private val repository: StatisticsRepository
) : ViewModel() {

    private val _fairingRecovery = MutableLiveData<ApiResult<List<FairingRecoveryModel>>>()
    val fairingRecovery: LiveData<ApiResult<List<FairingRecoveryModel>>> = _fairingRecovery

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun get(cachePolicy: CachePolicy = CachePolicy.EXPIRES) {
        viewModelScope.launch {
            val response = async(_fairingRecovery) {
                repository.fetch(
                    key = "fairing",
                    query = StatisticsQuery.fairingQuery,
                    cachePolicy = cachePolicy
                )
            }

            val result = response.await().map { launches -> launches.docs.map { Launch(it) } }

            _fairingRecovery.value = result.map { launches ->
                ArrayList<FairingRecoveryModel>().apply {
                    launches.forEach {
                        val year = it.launchDate?.dateUnix?.formatDateMillisYYYY() ?: return@forEach

                        if (none { newList -> newList.year == year }) add(FairingRecoveryModel(year))

                        val stat = first { newList -> newList.year == year }

                        if (it.fairings?.recoveryAttempt == true && it.fairings.isRecovered == true) {
                            stat.successes++
                        } else stat.failures++
                    }
                }
            }
        }
    }
}
