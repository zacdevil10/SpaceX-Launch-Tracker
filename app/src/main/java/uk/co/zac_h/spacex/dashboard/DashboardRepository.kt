package uk.co.zac_h.spacex.dashboard

import androidx.lifecycle.LiveData
import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXService
import uk.co.zac_h.spacex.utils.BaseNetwork
import uk.co.zac_h.spacex.utils.repo.DashboardSharedPreferencesService
import uk.co.zac_h.spacex.utils.repo.PinnedPreferencesService
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val service: SpaceXService,
    private val prefs: DashboardSharedPreferencesService,
    private val pinned: PinnedPreferencesService
) : BaseNetwork() {

    private var call: Call<LaunchDocsModel>? = null

    fun queryLaunches(id: String, listener: ResponseCallback<Launch?>): Unit? {
        call = service.queryLaunches(query(id)).apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(response.body()?.docs?.get(0)?.let { Launch(it) }, id)
                }
                onResponseFailure = { listener.onError() }
            }
        }
    }

    fun getPinnedLiveData() = pinned.pinnedLive

    fun removePinnedLaunch(id: String) = pinned.removePinnedLaunch(id)

    fun getVisibleLiveData(): LiveData<MutableMap<String, *>> = prefs.visibleLiveData

    fun visible(section: String) = prefs.visible(section)

    var isVisible: MutableMap<String, *>? = null
        set(value) {
            field = value
            if (value != null) {
                prefs.isVisible = value
            }
        }

    private fun query(id: String) = QueryModel(
        query = when (id) {
            "next", "latest" -> QueryUpcomingLaunchesModel(id == "next")
            else -> QueryLaunchesQueryModel(id)
        },
        options = QueryOptionsModel(
            pagination = true,
            populate = listOf(
                QueryPopulateModel(path = "rocket", populate = "", select = listOf("name")),
                QueryPopulateModel("launchpad", select = listOf("name"), populate = ""),
                QueryPopulateModel("crew", populate = "", select = listOf("id")),
                QueryPopulateModel("ships", populate = "", select = listOf("id")),
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
            sort = when (id) {
                "next" -> QueryLaunchesSortModel("asc")
                "latest" -> QueryLaunchesSortModel("desc")
                else -> ""
            },
            select = listOf(
                "flight_number",
                "name",
                "date_unix",
                "tbd",
                "rocket",
                "cores",
                "crew",
                "ships",
                "links",
                "static_fire_date_unix",
                "details",
                "launchpad",
                "date_precision"
            ),
            limit = 1
        )
    )

}