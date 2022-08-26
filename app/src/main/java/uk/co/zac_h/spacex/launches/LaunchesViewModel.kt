package uk.co.zac_h.spacex.launches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.async
import uk.co.zac_h.spacex.query.LaunchQuery
import uk.co.zac_h.spacex.types.LaunchType
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val repository: LaunchesRepository
) : ViewModel() {

    private val _launchesLiveData =
        MutableLiveData<ApiResult<Map<LaunchType, List<Launch>>>>()
    val launchesLiveData: LiveData<ApiResult<Map<LaunchType, List<Launch>>>> = _launchesLiveData

    fun getLaunches(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_launchesLiveData) {
                _launchesLiveData.value = ApiResult.Pending
                repository.fetch(
                    key = "launches",
                    query = LaunchQuery.launchesQuery,
                    cachePolicy = cachePolicy
                )
            }

            _launchesLiveData.value = response.await().map { data ->
                data.docs.map { Launch(it) }.groupBy {
                    if (it.upcoming == true) LaunchType.UPCOMING else LaunchType.PAST
                }
            }
        }
    }

}