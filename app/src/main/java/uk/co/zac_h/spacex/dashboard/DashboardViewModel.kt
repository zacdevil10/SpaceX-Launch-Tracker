package uk.co.zac_h.spacex.dashboard

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.async
import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.query.LaunchQuery
import uk.co.zac_h.spacex.utils.repo.PinnedPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository,
    pinnedPreferencesRepository: PinnedPreferencesRepository
) : ViewModel() {

    private val _launches = MutableLiveData<ApiResult<List<Launch>>>()

    val pinnedLaunches: LiveData<ApiResult<List<Launch>>> =
        pinnedPreferencesRepository.pinnedLive.switchMap { pinned ->
            _launches.map { response ->
                response.map { launches ->
                    launches.filter { launch ->
                        launch.id in pinned.keys && pinned[launch.id] == true
                    }
                }
            }
        }

    val nextLaunch: LiveData<ApiResult<Launch>> = _launches.map { result ->
        result.map {
            it.sortedBy { launch -> launch.flightNumber }
                .first { launch -> launch.upcoming == true }
        }
    }

    val latestLaunch: LiveData<ApiResult<Launch>> = _launches.map { result ->
        result.map {
            it.sortedBy { launch -> launch.flightNumber }
                .last { launch -> launch.upcoming == false }
        }
    }

    val dashboardLiveData = repository.allPreferences.map { it }

    fun getLaunches(cachePolicy: CachePolicy = CachePolicy.EXPIRES) {
        viewModelScope.launch {
            val response = async(_launches) {
                repository.fetch(key = "launches", query = LaunchQuery.launchesQuery, cachePolicy = cachePolicy)
            }

            val launches = response.await().map { docsModel ->
                docsModel.docs.map { Launch(it) }
            }

            _launches.value = launches
        }
    }

    fun getDashboardSectionState(section: String) = repository.getSectionState(section)

    fun showDashboardSection(section: String) {
        repository.updateSection(section, true)
    }

    fun hideDashboardSection(section: String) {
        repository.updateSection(section, false)
    }

}