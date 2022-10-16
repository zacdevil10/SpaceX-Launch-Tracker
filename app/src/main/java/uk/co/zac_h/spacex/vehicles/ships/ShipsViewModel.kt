package uk.co.zac_h.spacex.vehicles.ships

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.types.Order
import uk.co.zac_h.spacex.core.utils.sortedBy
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.Repository
import uk.co.zac_h.spacex.network.async
import uk.co.zac_h.spacex.network.query.VehicleQuery
import javax.inject.Inject

@HiltViewModel
class ShipsViewModel @Inject constructor(
    private val repository: ShipsRepository
) : ViewModel() {

    private val _ships = MutableLiveData<ApiResult<List<Ship>>>()
    val ships: LiveData<ApiResult<List<Ship>>> = _ships.map { result ->
        result.map {
            it.sortedBy(order) { ship ->
                ship.yearBuilt
            }
        }
    }

    private var order: Order = Order.ASCENDING

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun getShips(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_ships) {
                repository.fetch(
                    key = "rockets",
                    query = VehicleQuery.shipQuery,
                    cachePolicy = cachePolicy
                )
            }

            _ships.value = response.await().map { it.docs.map { ship -> Ship(ship) } }
        }
    }

    fun setOrder(order: Order?) {
        this.order = order ?: Order.ASCENDING
    }
}
