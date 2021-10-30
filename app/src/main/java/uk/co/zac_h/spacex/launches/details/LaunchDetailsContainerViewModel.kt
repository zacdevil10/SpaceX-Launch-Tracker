package uk.co.zac_h.spacex.launches.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.async
import uk.co.zac_h.spacex.dto.spacex.QueryLaunchesQueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel
import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.launches.LaunchesRepository
import uk.co.zac_h.spacex.utils.repo.PinnedPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class LaunchDetailsContainerViewModel @Inject constructor(
    private val repository: LaunchesRepository,
    private val pinnedPreferencesRepository: PinnedPreferencesRepository
) : ViewModel() {

    private val _launch = MutableLiveData<ApiResult<Launch>>()
    val launch: LiveData<ApiResult<Launch>> = _launch

    var launchID: String = ""

    fun getLaunch(cachePolicy: CachePolicy = CachePolicy.EXPIRES) {
        viewModelScope.launch {
            val response = async(_launch) {
                repository.fetch(key = launchID, query = query, cachePolicy = cachePolicy)
            }

            _launch.value = response.await().map { Launch(it.docs.first()) }
        }
    }

    fun isPinned() = pinnedPreferencesRepository.isPinned(launchID)

    fun pinLaunch(pinned: Boolean) {
        pinnedPreferencesRepository.setPinnedLaunch(launchID, pinned)
    }

    private val query: QueryModel
        get() = QueryModel(
            query = QueryLaunchesQueryModel(_id = launchID),
            options = QueryOptionsModel(
                false,
                populate = listOf(
                    QueryPopulateModel("launchpad", select = listOf("name"), populate = ""),
                    QueryPopulateModel("rocket", populate = "", select = listOf("name")),
                    QueryPopulateModel(
                        "cores",
                        populate = listOf(
                            QueryPopulateModel(
                                "core",
                                populate = listOf(
                                    QueryPopulateModel(
                                        "launches",
                                        select = listOf("name", "flight_number"),
                                        populate = ""
                                    )
                                ),
                                select = ""
                            ),
                            QueryPopulateModel(
                                "landpad",
                                select = "",
                                populate = listOf(
                                    QueryPopulateModel(
                                        "launches",
                                        select = listOf("id"),
                                        populate = ""
                                    )
                                )
                            )
                        ),
                        select = ""
                    ),
                    QueryPopulateModel(
                        "crew.crew",
                        populate = listOf(
                            QueryPopulateModel(
                                "launches",
                                populate = "",
                                select = listOf("flight_number", "name", "date_unix")
                            )
                        ),
                        select = ""
                    ),
                    QueryPopulateModel("payloads", populate = "", select = ""),
                    QueryPopulateModel(
                        "ships",
                        populate = listOf(
                            QueryPopulateModel(
                                "launches",
                                populate = "",
                                select = listOf("name", "flight_number")
                            )
                        ),
                        select = ""
                    )
                ),
                sort = "",
                select = listOf(
                    "links",
                    "static_fire_date_unix",
                    "tbd",
                    "net",
                    "rocket",
                    "details",
                    "launchpad",
                    "flight_number",
                    "name",
                    "date_unix",
                    "date_precision",
                    "upcoming",
                    "cores",
                    "crew",
                    "payloads",
                    "ships"
                ),
                limit = 1
            )
        )
}