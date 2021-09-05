package uk.co.zac_h.spacex.vehicles.cores

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
import uk.co.zac_h.spacex.dto.spacex.Core
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel
import uk.co.zac_h.spacex.utils.reverse
import javax.inject.Inject

@HiltViewModel
class CoreViewModel @Inject constructor(
    private val repository: CoreRepository
) : ViewModel() {

    private val _cores = MutableLiveData<ApiResult<List<Core>>>()
    val cores: LiveData<ApiResult<List<Core>>> = _cores

    var selectedId = ""

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun getCores(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_cores) {
                repository.fetch(key = "cores", query = query, cachePolicy = cachePolicy)
            }

            _cores.value = response.await().map {
                it.docs.map { core -> Core(core) }.reverse(getOrder())
            }
        }
    }

    fun getOrder() = repository.getOrder()

    fun setOrder(order: Boolean) = repository.setOrder(order)

    private val query = QueryModel(
        options = QueryOptionsModel(
            pagination = false,
            populate = listOf(
                QueryPopulateModel(
                    "launches",
                    select = listOf("name", "flight_number"),
                    populate = ""
                )
            ),
            limit = 100000
        )
    )

}