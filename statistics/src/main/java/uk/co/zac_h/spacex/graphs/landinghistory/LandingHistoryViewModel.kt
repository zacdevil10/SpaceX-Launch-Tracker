package uk.co.zac_h.spacex.graphs.landinghistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.*
import uk.co.zac_h.spacex.dto.spacex.*
import uk.co.zac_h.spacex.models.LandingHistoryModel
import uk.co.zac_h.spacex.utils.formatDateMillisYYYY
import javax.inject.Inject

@HiltViewModel
class LandingHistoryViewModel @Inject constructor(
    private val repository: StatisticsRepository
) : ViewModel() {

    private val _landingHistory = MutableLiveData<ApiResult<List<LandingHistoryModel>>>()
    val landingHistory: LiveData<ApiResult<List<LandingHistoryModel>>> = _landingHistory

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun get(cachePolicy: CachePolicy = CachePolicy.EXPIRES) {
        viewModelScope.launch {
            val response = async(_landingHistory) {
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