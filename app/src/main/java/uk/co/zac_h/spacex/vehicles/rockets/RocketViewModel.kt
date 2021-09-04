package uk.co.zac_h.spacex.vehicles.rockets

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.async
import uk.co.zac_h.spacex.dto.spacex.Rocket
import javax.inject.Inject

@HiltViewModel
class RocketViewModel @Inject constructor(
    private val repository: RocketRepository
) : ViewModel() {

    private val _rockets = MutableLiveData<ApiResult<List<Rocket>>>()
    val rockets: LiveData<ApiResult<List<Rocket>>> = _rockets

    private val _selected = MutableLiveData("")

    val rocket: LiveData<Rocket?> = _selected.switchMap { selected ->
        _rockets.map {
            it.data?.firstOrNull { data -> data.id == selected }
        }
    }

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun getRockets(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_rockets) {
                repository.fetch(key = "rockets", cachePolicy = cachePolicy)
            }

            _rockets.value = response.await().map { result -> result.map { Rocket(it) } }
        }
    }

    fun setSelected(id: String) {
        _selected.value = id
    }

}