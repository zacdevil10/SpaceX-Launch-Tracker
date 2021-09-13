package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.local.DashboardPreferencesDataSource
import uk.co.zac_h.spacex.datasource.remote.LaunchesDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.LaunchDocsModel
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    @LaunchesDataSourceClient launchesDataSource: RemoteDataSource<LaunchDocsModel>,
    cache: Cache<LaunchDocsModel>,
    private val preferences: DashboardPreferencesDataSource
) : Repository<LaunchDocsModel>(launchesDataSource, cache) {

    val allPreferences = preferences.allLiveData

    fun getSectionState(section: String): Boolean = preferences.getValue(section)

    fun updateSection(section: String, visible: Boolean) {
        preferences.add(section, visible)
    }

}