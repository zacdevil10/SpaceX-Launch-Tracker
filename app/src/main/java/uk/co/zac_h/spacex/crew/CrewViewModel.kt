package uk.co.zac_h.spacex.crew

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
import uk.co.zac_h.spacex.dto.spacex.Crew
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel
import javax.inject.Inject

@HiltViewModel
class CrewViewModel @Inject constructor(
    private val repository: CrewRepository
) : ViewModel() {

    private val _crew = MutableLiveData<ApiResult<List<Crew>>>()
    val crew: LiveData<ApiResult<List<Crew>>> = _crew

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun get(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_crew) {
                repository.fetch("crew", query = query, cachePolicy = cachePolicy)
            }

            _crew.value = response.await().map { result -> result.docs.map { Crew(it) } }
        }
    }

    private val query = QueryModel(
        "",
        QueryOptionsModel(
            false,
            listOf(
                QueryPopulateModel(
                    "launches",
                    select = listOf("flight_number", "name", "date_unix"),
                    populate = ""
                )
            ), "", "", 100000
        )
    )

}