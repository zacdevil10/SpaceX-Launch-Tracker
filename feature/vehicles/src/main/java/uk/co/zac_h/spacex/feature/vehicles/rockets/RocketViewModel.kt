package uk.co.zac_h.spacex.feature.vehicles.rockets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.common.types.Order
import uk.co.zac_h.spacex.core.common.utils.sortedBy
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.Repository
import uk.co.zac_h.spacex.network.async
import uk.co.zac_h.spacex.network.dto.spacex.LauncherResponse
import javax.inject.Inject

@HiltViewModel
class RocketViewModel @Inject constructor(
    private val repository: RocketRepository
) : ViewModel() {

    private val _rockets = MutableLiveData<ApiResult<List<LauncherItem>>>()
    val rockets: LiveData<ApiResult<List<LauncherItem>>> = _rockets.map { result ->
        result.map {
            it.sortedBy(order) { rocket -> rocket.maidenFlightMillis }
        }
    }

    private var order: Order = Order.ASCENDING

    var hasOrderChanged = false

    val launcherLiveData: LiveData<PagingData<LauncherResponse>>
        get() = Pager(
            PagingConfig(pageSize = 10)
        ) {
            repository.launcherPagingSource
        }.liveData.cachedIn(viewModelScope)

    var selectedLauncher: LauncherItem? = null
        set(value) {
            field = value
            repository.launcherConfigId = value?.id
        }

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun getRockets(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_rockets) {
                repository.fetch(key = "agency", cachePolicy = cachePolicy)
            }

            _rockets.value = response.await().map { result ->
                result.launcherList?.map { LauncherItem(it) } ?: emptyList()
            }
        }
    }

    fun setOrder(order: Order?) {
        hasOrderChanged = true
        this.order = order ?: Order.ASCENDING
    }
}
