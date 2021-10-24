package uk.co.zac_h.spacex.launches.filter

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.async
import uk.co.zac_h.spacex.dto.spacex.Launch
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel
import uk.co.zac_h.spacex.launches.LaunchesRepository
import javax.inject.Inject

@HiltViewModel
class LaunchesFilterViewModel @Inject constructor(
    private val repository: LaunchesRepository,
    val filterClass: LaunchesFilterTest
) : ViewModel() {

    private val _launchesLiveData = MutableLiveData<ApiResult<List<Launch>>>()
    val launchesLiveData: LiveData<ApiResult<List<Launch>>> =
        _launchesLiveData.switchMap { result ->
            filterClass.map { filter ->
                result.map {
                    it.filterAll(
                        {
                            filter.search.filter()?.let { searchTerm ->
                                it.missionName?.lowercase()?.contains(searchTerm)
                            }
                        },
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
                    ).sortedBy(filter.order) { launch ->
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
        direction: LaunchesFilterOrder,
        crossinline selector: (T) -> R?
    ): List<T> {
        return when (direction) {
            LaunchesFilterOrder.ASCENDING -> sortedWith(compareBy(selector))
            LaunchesFilterOrder.DESCENDING -> sortedWith(compareByDescending(selector))
        }
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