package uk.co.zac_h.spacex.launches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.dto.spacex.Launch
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val repository: LaunchesRepository
) : ViewModel() {

    private val _launchesLiveData =
        MutableLiveData<ApiResult<Map<LaunchType, List<Launch>>>>(ApiResult.pending())
    val launchesLiveData: LiveData<ApiResult<Map<LaunchType, List<Launch>>>> = _launchesLiveData

    fun getLaunches(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async {
                _launchesLiveData.value = ApiResult.pending()
                repository.fetch(
                    key = "launches",
                    query = query,
                    cachePolicy = cachePolicy
                )
            }

            _launchesLiveData.value = response.await().map { data ->
                data.docs.map { Launch(it) }.groupBy {
                    if (it.upcoming == true) LaunchType.UPCOMING else LaunchType.PAST
                }
            }
        }
    }

    private val query = QueryModel(
        options = QueryOptionsModel(
            pagination = false,
            populate = listOf(
                QueryPopulateModel(path = "rocket", populate = "", select = listOf("name")),
                QueryPopulateModel(
                    path = "cores",
                    populate = listOf(
                        QueryPopulateModel(
                            path = "landpad",
                            populate = "",
                            select = listOf("name")
                        ),
                        QueryPopulateModel(
                            path = "core",
                            populate = "",
                            select = listOf("reuse_count")
                        )
                    ),
                    select = ""
                )
            ),
            select = listOf(
                "flight_number",
                "name",
                "date_unix",
                "rocket",
                "cores",
                "links",
                "date_precision",
                "upcoming",
                "tbd"
            ),
            limit = 10000
        )
    )
}