package uk.co.zac_h.spacex.vehicles.cores

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.common.types.Order
import uk.co.zac_h.spacex.core.common.utils.sortedBy
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.Repository
import uk.co.zac_h.spacex.network.async
import uk.co.zac_h.spacex.network.query.VehicleQuery
import javax.inject.Inject

@HiltViewModel
class CoreViewModel @Inject constructor(
    private val repository: CoreRepository
) : ViewModel() {

    private val _cores = MutableLiveData<ApiResult<List<Core>>>()
    val cores: LiveData<ApiResult<List<Core>>> = _cores.map { result ->
        result.map {
            it.sortedBy(order) { core ->
                core.id
            }
        }
    }

    var order: Order = repository.getOrder()
        get() = repository.getOrder()
        set(value) {
            field = value
            repository.setOrder(value)
        }

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun getCores(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_cores) {
                repository.fetch(
                    key = "cores",
                    query = VehicleQuery.coreQuery,
                    cachePolicy = cachePolicy
                )
            }

            _cores.value = response.await().map {
                it.docs.map { core -> Core(core) }
            }
        }
    }
}
