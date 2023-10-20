package uk.co.zac_h.spacex.feature.assets.vehicles.rockets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.common.utils.filterAll
import uk.co.zac_h.spacex.core.common.utils.sortedBy
import uk.co.zac_h.spacex.feature.assets.vehicles.rockets.filter.RocketsFilterBuilder
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.Repository
import uk.co.zac_h.spacex.network.async
import javax.inject.Inject

@HiltViewModel
class RocketViewModel @Inject constructor(
    private val repository: RocketRepository
) : ViewModel() {

    val filter: RocketsFilterBuilder = RocketsFilterBuilder()

    private val _rockets = MutableLiveData<ApiResult<List<RocketItem>>>()
    val rockets: LiveData<ApiResult<List<RocketItem>>> = _rockets.switchMap { result ->
        filter.map { filter ->
            result.map {
                it.filterAll(
                    if (filter.family.isFiltered) { rocket ->
                        rocket.family == filter.family.family
                    } else null,
                    if (filter.type.isFiltered) { rocket ->
                        rocket.type in filter.type.rockets.orEmpty()
                    } else null
                ).sortedBy(filter.order.order) { rocket -> rocket.maidenFlightMillis }
            }
        }.distinctUntilChanged()
    }

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun getRockets(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_rockets) {
                repository.fetch(key = "agency", cachePolicy = cachePolicy)
            }

            _rockets.value = response.await().map { result ->
                result.launcherList?.map { RocketItem(it) } ?: emptyList()
            }
        }
    }
}
