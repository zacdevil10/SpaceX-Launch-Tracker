package uk.co.zac_h.spacex.vehicles.capsules

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.async
import uk.co.zac_h.spacex.query.VehicleQuery
import uk.co.zac_h.spacex.types.Order
import uk.co.zac_h.spacex.utils.sortedBy
import javax.inject.Inject

@HiltViewModel
class CapsulesViewModel @Inject constructor(
    private val repository: CapsulesRepository
) : ViewModel() {

    private val _capsules = MutableLiveData<ApiResult<List<Capsule>>>()
    val capsules: LiveData<ApiResult<List<Capsule>>> = _capsules.map { result ->
        result.map {
            it.sortedBy(order) { capsule ->
                capsule.serial?.drop(0)
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

    fun get(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_capsules) {
                repository.fetch(
                    key = "capsules",
                    query = VehicleQuery.capsuleQuery,
                    cachePolicy = cachePolicy
                )
            }

            _capsules.value = response.await().map {
                it.docs.map { capsule -> Capsule(capsule) }
            }
        }
    }
}
