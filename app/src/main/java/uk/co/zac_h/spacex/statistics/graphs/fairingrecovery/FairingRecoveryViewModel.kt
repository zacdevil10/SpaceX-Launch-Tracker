package uk.co.zac_h.spacex.statistics.graphs.fairingrecovery

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
import uk.co.zac_h.spacex.utils.models.FairingRecoveryModel
import javax.inject.Inject

@HiltViewModel
class FairingRecoveryViewModel @Inject constructor(
    private val repository: StatisticsRepository
) : ViewModel() {

    private val _fairingRecovery = MutableLiveData<ApiResult<List<FairingRecoveryModel>>>(ApiResult.pending())
    val fairingRecovery: LiveData<ApiResult<List<FairingRecoveryModel>>> = _fairingRecovery

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun get(cachePolicy: CachePolicy = CachePolicy.EXPIRES) {
        viewModelScope.launch {
            val response = async {
                repository.fetch(key = "fairing", query = query, cachePolicy = cachePolicy)
            }

            val result = response.await().map { it.docs.map { Launch(it) } }

            _fairingRecovery.value = result.map { launches ->
                val stats = ArrayList<FairingRecoveryModel>()

                launches.forEach { launch ->
                    val year = launch.launchDate?.dateUnix?.formatDateMillisYYYY() ?: return@forEach

                    if (stats.none { it.year == year }) stats.add(FairingRecoveryModel(year))

                    val stat = stats.filter { it.year == year }[0]

                    when (launch.fairings?.recoveryAttempt == true && launch.fairings?.isRecovered == true) {
                        true -> stat.successes++
                        false -> stat.failures++
                    }
                }

                stats
            }
        }
    }

    private val query = QueryModel(
        QueryFairingRecovery(true),
        QueryOptionsModel(
            false,
            "",
            QueryLaunchesSortByDate("asc"),
            listOf("fairings", "date_unix"),
            1000000
        )
    )

}