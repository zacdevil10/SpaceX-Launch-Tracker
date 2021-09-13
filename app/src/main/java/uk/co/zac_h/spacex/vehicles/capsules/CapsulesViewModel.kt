package uk.co.zac_h.spacex.vehicles.capsules

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.*
import uk.co.zac_h.spacex.dto.spacex.Capsule
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel
import uk.co.zac_h.spacex.utils.reverse
import javax.inject.Inject

@HiltViewModel
class CapsulesViewModel @Inject constructor(
    private val repository: CapsulesRepository
) : ViewModel() {

    private val _capsules = MutableLiveData<ApiResult<List<Capsule>>>()
    val capsules: LiveData<ApiResult<List<Capsule>>> = _capsules

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun get(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_capsules) {
                repository.fetch(key = "capsules", query = query, cachePolicy = cachePolicy)
            }

            _capsules.value = response.await().map {
                it.docs.map { capsule -> Capsule(capsule) }.reverse(getOrder())
            }
        }
    }

    fun getOrder() = repository.getOrder()

    fun setOrder(order: Boolean) = repository.setOrder(order)

    private val query = QueryModel(
        "",
        QueryOptionsModel(
            false, listOf(
                QueryPopulateModel(
                    SPACEX_FIELD_CAPSULE_LAUNCHES,
                    select = listOf(
                        SPACEX_FIELD_LAUNCH_NAME,
                        SPACEX_FIELD_LAUNCH_FLIGHT_NUMBER
                    ),
                    populate = ""
                )
            ), "", "", 100000
        )
    )

}