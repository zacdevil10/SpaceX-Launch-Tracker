package uk.co.zac_h.spacex.dashboard

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.utils.BaseNetwork
import uk.co.zac_h.spacex.utils.repo.DashboardObject

class DashboardViewModel @ViewModelInject constructor(
    private val repository: DashboardRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel(), BaseNetwork.ResponseCallback<Launch?> {

    init {
        repository.queryLaunches("next", this)
        repository.queryLaunches("latest", this)
    }

    private val _nextLaunch = MutableLiveData<Launch>()
    val nextLaunch: LiveData<Launch> = _nextLaunch

    private val _latestLaunch = MutableLiveData<Launch>()
    val latestLaunch: LiveData<Launch> = _latestLaunch

    val pinnedIds: LiveData<MutableMap<String, Boolean>> =
        Transformations.map(repository.getPinnedLiveData()) {
            it
        }

    private val _pinnedLaunches = MutableLiveData<MutableMap<String, Launch>>()
    val pinnedLaunches: LiveData<MutableMap<String, Launch>> = _pinnedLaunches

    val visible: LiveData<MutableMap<String, Boolean>> =
        Transformations.map(repository.getVisibleLiveData()) { visible ->
            with(DashboardObject) {
                mutableMapOf(
                    PREFERENCES_NEXT_LAUNCH to (visible[PREFERENCES_NEXT_LAUNCH] as Boolean?
                        ?: true),
                    PREFERENCES_PREVIOUS_LAUNCH to (visible[PREFERENCES_PREVIOUS_LAUNCH] as Boolean?
                        ?: true),
                    PREFERENCES_PINNED_LAUNCH to (visible[PREFERENCES_PINNED_LAUNCH] as Boolean?
                        ?: true)
                )
            }
        }

    val countdown = MutableLiveData<String?>(null)

    private val _nextLoading = MutableLiveData(true)
    val nextLoading: LiveData<Boolean> = _nextLoading

    private val _latestLoading = MutableLiveData(true)
    val latestLoading: LiveData<Boolean> = _latestLoading

    private val _pinnedLoading = MutableLiveData(true)
    val pinnedLoading: LiveData<Boolean> = _pinnedLoading

    private val _pinnedEmpty = MutableLiveData(true)
    val pinnedEmpty: LiveData<Boolean> = _pinnedEmpty

    val refreshing = MutableLiveData(false)

    override fun onSuccess(response: Launch?, id: String) {
        when (id) {
            "next" -> {
                _nextLaunch.value = response
                _nextLoading.value = false
            }
            "latest" -> {
                _latestLaunch.value = response
                _latestLoading.value = false
            }
            else -> response?.let { _pinnedLaunches.value?.put(id, response) }
        }
    }

    override fun onError() {

    }

}