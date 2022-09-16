package uk.co.zac_h.spacex.launches.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.launches.LaunchesRepository
import uk.co.zac_h.spacex.query.LaunchQuery
import uk.co.zac_h.spacex.utils.repo.PinnedPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class LaunchDetailsContainerViewModel @Inject constructor(
    private val repository: LaunchesRepository,
    private val pinnedPreferencesRepository: PinnedPreferencesRepository
) : ViewModel() {

    private val _launch = MutableLiveData<ApiResult<Launch>>()
    val launch: LiveData<ApiResult<Launch>> = _launch

    var launchID: String = ""

    fun getLaunch(cachePolicy: CachePolicy = CachePolicy.EXPIRES) {
        viewModelScope.launch {
            val response = async {
                repository.fetch(
                    key = "launches",
                    query = LaunchQuery.launchesQuery,
                    cachePolicy = cachePolicy
                )
            }

            _launch.value = response.await().map { docsModel ->
                Launch(docsModel.docs.first { it.id == launchID })
            }
        }
    }

    fun isPinned() = pinnedPreferencesRepository.isPinned(launchID)

    fun pinLaunch(pinned: Boolean) {
        pinnedPreferencesRepository.setPinnedLaunch(launchID, pinned)
    }
}
