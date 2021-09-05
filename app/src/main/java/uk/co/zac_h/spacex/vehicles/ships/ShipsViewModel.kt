package uk.co.zac_h.spacex.vehicles.ships

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.async
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel
import uk.co.zac_h.spacex.dto.spacex.Ship
import javax.inject.Inject

@HiltViewModel
class ShipsViewModel @Inject constructor(
    private val repository: ShipsRepository
) : ViewModel() {

    private val _ships = MutableLiveData<ApiResult<List<Ship>>>()
    val ships: LiveData<ApiResult<List<Ship>>> = _ships

    var selectedId = ""

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

}