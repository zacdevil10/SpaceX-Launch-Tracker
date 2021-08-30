package uk.co.zac_h.spacex.dashboard

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.dto.spacex.*
import uk.co.zac_h.spacex.utils.repo.PinnedPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository,
    pinnedPreferencesRepository: PinnedPreferencesRepository
) : ViewModel() {

    private val _nextLaunch = MutableLiveData<ApiResult<Launch>>(ApiResult.pending())
    val nextLaunch: LiveData<ApiResult<Launch>> = _nextLaunch

    private val _latestLaunch = MutableLiveData<ApiResult<Launch>>(ApiResult.pending())
    val latestLaunch: LiveData<ApiResult<Launch>> = _latestLaunch

    private val _pinnedLaunches = MutableLiveData<ArrayList<ApiResult<Launch>>>()
    val pinnedLaunches: LiveData<ArrayList<ApiResult<Launch>>> = _pinnedLaunches
    val pinned: ArrayList<ApiResult<Launch>> = ArrayList()

    val pinnedLiveData = pinnedPreferencesRepository.pinnedLive/*.map {
        it.filterNot { item ->
            val keep = item.value as Boolean
            if (!keep) pinnedPreferencesRepository.removePinnedLaunch(item.key)
            keep
        }
    }*/

    val dashboardLiveData = repository.allPreferences.map { it }

    fun getLaunch(id: String) {
        viewModelScope.launch {
            val response = async {
                repository.fetch(
                    key = id,
                    query = query(
                        when (id) {
                            "next" -> Upcoming.NEXT
                            "latest" -> Upcoming.LATEST
                            else -> id
                        }
                    ),
                    cachePolicy = when (id) {
                        "next" -> CachePolicy.EXPIRES
                        else -> CachePolicy.ALWAYS
                    }
                )
            }

            val launch = response.await().map { Launch(it.docs.first()) }

            when (id) {
                "next" -> _nextLaunch.value = launch
                "latest" -> _latestLaunch.value = launch
                else -> {
                    pinned.add(launch)
                    _pinnedLaunches.value = pinned
                }
            }
        }
    }

    fun getDashboardSectionState(section: String) = repository.getSectionState(section)

    fun showDashboardSection(section: String) {
        repository.updateSection(section, true)
    }

    fun hideDashboardSection(section: String) {
        repository.updateSection(section, false)
    }

    private fun query(data: Any) = QueryModel(
        query = when (data) {
            is Upcoming -> QueryUpcomingLaunchesModel(data.upcoming)
            else -> QueryLaunchesQueryModel(data as String)
        },
        options = QueryOptionsModel(
            pagination = true,
            populate = listOf(
                QueryPopulateModel(path = "rocket", populate = "", select = listOf("name")),
                QueryPopulateModel("launchpad", select = listOf("name"), populate = ""),
                QueryPopulateModel("crew.crew", populate = "", select = listOf("id")),
                QueryPopulateModel("ships", populate = "", select = listOf("id")),
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
            sort = when (data) {
                Upcoming.NEXT -> QueryLaunchesSortModel("asc")
                Upcoming.LATEST -> QueryLaunchesSortModel("desc")
                else -> ""
            },
            select = listOf(
                "flight_number",
                "name",
                "date_unix",
                "tbd",
                "rocket",
                "cores",
                "crew",
                "ships",
                "links",
                "static_fire_date_unix",
                "details",
                "launchpad",
                "date_precision",
                "upcoming"
            ),
            limit = 1
        )
    )

}