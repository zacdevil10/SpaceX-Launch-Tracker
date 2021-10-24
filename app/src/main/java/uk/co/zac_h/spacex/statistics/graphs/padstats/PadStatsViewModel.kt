package uk.co.zac_h.spacex.statistics.graphs.padstats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.async
import uk.co.zac_h.spacex.dto.spacex.*
import javax.inject.Inject

@HiltViewModel
class PadStatsViewModel @Inject constructor(
    private val launchpadRepository: LaunchpadRepository,
    private val landingPadRepository: LandingPadRepository
) : ViewModel() {

    private val _stats = MutableLiveData<ApiResult<List<StatsPadModel>>>()
    val stats: LiveData<ApiResult<List<StatsPadModel>>> = _stats

    fun getLaunchpads(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_stats) {
                launchpadRepository.fetch(key = "launchpads", query = launchQuery, cachePolicy = cachePolicy)
            }

            val result = response.await().map { launches -> launches.docs.map { Launchpad(it) } }

            _stats.value = result.map { list ->
                list.map {
                    StatsPadModel(
                        it.fullName,
                        it.launchAttempts ?: 0,
                        it.launchSuccesses ?: 0,
                        it.status
                    )
                }
            }
        }
    }

    fun getLandingPads(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_stats) {
                landingPadRepository.fetch(key = "landing_pads", query = landQuery, cachePolicy = cachePolicy)
            }

            val result = response.await().map { launches -> launches.docs.map { LandingPad(it) } }

            _stats.value = result.map { list ->
                list.map {
                    StatsPadModel(
                        it.fullName,
                        it.landingAttempts ?: 0,
                        it.landingSuccesses ?: 0,
                        it.status,
                        it.type
                    )
                }
            }
        }
    }

    private val launchQuery = QueryModel(
        "",
        QueryOptionsModel(
            false,
            "",
            "",
            listOf("full_name", "launch_attempts", "launch_successes", "status"),
            100000
        )
    )

    private val landQuery = QueryModel(
        "",
        QueryOptionsModel(
            false,
            "",
            "",
            listOf("full_name", "landing_attempts", "landing_successes", "status", "type"),
            100000
        )
    )

}