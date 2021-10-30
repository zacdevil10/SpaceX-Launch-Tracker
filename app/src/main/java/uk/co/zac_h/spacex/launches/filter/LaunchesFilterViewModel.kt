package uk.co.zac_h.spacex.launches.filter

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.async
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel
import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.launches.LaunchesRepository
import uk.co.zac_h.spacex.types.Order
import javax.inject.Inject

@HiltViewModel
class LaunchesFilterViewModel @Inject constructor(
    private val repository: LaunchesRepository
) : ViewModel() {

    val filter: LaunchesFilterBuilder = LaunchesFilterBuilder()

    private val _launchesLiveData = MutableLiveData<ApiResult<List<Launch>>>()
    val launchesLiveData: LiveData<ApiResult<List<Launch>>> =
        _launchesLiveData.switchMap { result ->
            filter.map { filter ->
                result.map {
                    it.filterAll(
                        if (filter.search.isFiltered) { launch ->
                            launch.missionName?.lowercase()?.contains(filter.search.filter)
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
            }
        }

    fun getLaunches(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_launchesLiveData) {
                repository.fetch(key = "launches", query = query, cachePolicy = cachePolicy)
            }

            _launchesLiveData.value = response.await().map { data -> data.docs.map { Launch(it) } }
        }
    }

    private fun <T> Iterable<T>.filterAll(vararg predicates: ((T) -> Boolean?)?): List<T> {
        return filter { value ->
            predicates.filterNotNull().all { it(value) ?: true }
        }
    }

    private inline fun <T, R : Comparable<R>> Iterable<T>.sortedBy(
        direction: Order,
        crossinline selector: (T) -> R?
    ): List<T> = when (direction) {
        Order.ASCENDING -> sortedWith(compareBy(selector))
        Order.DESCENDING -> sortedWith(compareByDescending(selector))
    }

    private val query = QueryModel(
        options = QueryOptionsModel(
            pagination = false,
            populate = listOf(
                QueryPopulateModel(path = "rocket", populate = "", select = listOf("name")),
                QueryPopulateModel(
                    path = "cores",
                    populate = listOf(
                        QueryPopulateModel(
                            path = "landpad",
                            populate = "",
                            select = listOf("name")
                        ),
                        QueryPopulateModel(
                            path = "core",
                            populate = "",
                            select = listOf("reuse_count")
                        )
                    ),
                    select = ""
                )
            ),
            select = listOf(
                "flight_number",
                "name",
                "date_unix",
                "rocket",
                "cores",
                "links",
                "date_precision",
                "upcoming",
                "tbd"
            ),
            limit = 10000
        )
    )

}