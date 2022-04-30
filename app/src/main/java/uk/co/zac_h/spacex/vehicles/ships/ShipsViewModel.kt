package uk.co.zac_h.spacex.vehicles.ships

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.async
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel
import uk.co.zac_h.spacex.types.Order
import uk.co.zac_h.spacex.utils.sortedBy
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
                repository.fetch(key = "rockets", query = query, cachePolicy = cachePolicy)
            }

            _ships.value = response.await().map { it.docs.map { ship -> Ship(ship) } }
        }
    }

    private val query = QueryModel(
        query = "",
        options = QueryOptionsModel(
            false,
            populate = listOf(
                QueryPopulateModel(
                    path = "launches",
                    select = listOf("flight_number", "name"),
                    populate = ""
                )
            ),
            sort = "",
            select = "",
            limit = 200
        )
    )

    fun setOrder(order: Order?) {
        this.order = order ?: Order.ASCENDING
    }

}