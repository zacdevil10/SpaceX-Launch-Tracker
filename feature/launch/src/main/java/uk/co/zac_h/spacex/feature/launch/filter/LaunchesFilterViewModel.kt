package uk.co.zac_h.spacex.feature.launch.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.feature.launch.LaunchItem
import uk.co.zac_h.spacex.feature.launch.LaunchesRepository
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import javax.inject.Inject

@HiltViewModel
class LaunchesFilterViewModel @Inject constructor(
    private val repository: LaunchesRepository
) : ViewModel() {

    val filter: LaunchesFilterBuilder = LaunchesFilterBuilder()

    private val _launchesLiveData =
        MutableLiveData<ApiResult<List<LaunchItem>>>()
    val launchesLiveData: LiveData<ApiResult<List<LaunchItem>>> =
        _launchesLiveData.map { result ->
            result
            /*filter.map { filter ->
                result.map {
                    it.filterAll(
                        if (filter.search.isFiltered) { launch ->
                            launch.missionName?.lowercase()
                                ?.contains(filter.search.filter.lowercase())
                        } else null,
                        {
                            filter.dateRange.filter?.let { dateRange ->
                                it.launchDate?.dateUnix?.times(1000) in dateRange
                            }
                        },
                        if (filter.rocket.isFiltered) {
                            {
                                filter.rocket.rockets?.let { rockets ->
                                    it.rocket?.type in rockets
                                }
                            }
                        } else null
                    ).sortedBy(filter.order.order) { launch ->
                        launch.flightNumber
                    }
                }
            }*/
        }

    fun getLaunches(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        /*viewModelScope.launch {
            val response = async(_launchesLiveData) {
                repository.fetch(
                    key = "launches",
                    query = LaunchQuery.launchesQuery,
                    cachePolicy = cachePolicy
                )
            }

            _launchesLiveData.value = response.await().map { data -> data.docs.map { Launch(it) } }
        }*/
    }
}
