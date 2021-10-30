package uk.co.zac_h.spacex.dashboard

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
import uk.co.zac_h.spacex.utils.repo.PinnedPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository,
    pinnedPreferencesRepository: PinnedPreferencesRepository
) : ViewModel() {

    private val _nextLaunch = MutableLiveData<ApiResult<Launch>>()
    val nextLaunch: LiveData<ApiResult<Launch>> = _nextLaunch

    private val _latestLaunch = MutableLiveData<ApiResult<Launch>>()
    val latestLaunch: LiveData<ApiResult<Launch>> = _latestLaunch

    private val _pinnedLaunches = MutableLiveData<MutableList<ApiResult<Launch>>>()
    val pinnedLaunches: LiveData<MutableList<ApiResult<Launch>>> = _pinnedLaunches
    private val pinned: MutableList<ApiResult<Launch>> = mutableListOf()

    val pinnedLiveData = pinnedPreferencesRepository.pinnedLive.map {
        it.filter { item ->
            val keep = item.value as Boolean
            if (!keep) pinnedPreferencesRepository.removePinnedLaunch(item.key)
            keep
        }
    }

    val dashboardLiveData = repository.allPreferences.map { it }

    fun getLaunch(id: String, cachePolicy: CachePolicy = CachePolicy.EXPIRES) {
        viewModelScope.launch {
            val response = async(
                when (id) {
                    "next" -> _nextLaunch
                    "latest" -> _latestLaunch
                    else -> null
                }
            ) {
                repository.fetch(key = "launches", query = query, cachePolicy = cachePolicy)
            }

            val launch = response.await().map { docsModel ->
                Launch(
                    docsModel.docs.filter {
                        when (id) {
                            "next" -> it.upcoming == true
                            "latest" -> it.upcoming == false
                            else -> it.id == id
                        }
                    }.sortedBy { it.flightNumber }.let {
                        when (id) {
                            "next" -> it.first()
                            "latest" -> it.last()
                            else -> it.first()
                        }
                    }
                )
            }

            when (id) {
                "next" -> _nextLaunch.value = launch
                "latest" -> _latestLaunch.value = launch
                else -> {
                    if (pinned.none { it.data?.id == launch.data?.id }) pinned.add(launch)
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