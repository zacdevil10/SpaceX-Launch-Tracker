package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.local.DashboardPreferencesDataSource
import uk.co.zac_h.spacex.datasource.remote.LaunchesDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.LaunchQueriedResponse
import uk.co.zac_h.spacex.dto.spacex.NetworkDocsResponse
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    @LaunchesDataSourceClient launchesDataSource: RemoteDataSource<NetworkDocsResponse<LaunchQueriedResponse>>,
    cache: Cache<NetworkDocsResponse<LaunchQueriedResponse>>,
    private val preferences: DashboardPreferencesDataSource
) : Repository<NetworkDocsResponse<LaunchQueriedResponse>>(launchesDataSource, cache) {

    val allPreferences = preferences.allLiveData

    fun getSectionState(section: String): Boolean = preferences.getValue(section, true)

    fun updateSection(section: String, visible: Boolean) {
        preferences.add(section, visible)
    }
}
