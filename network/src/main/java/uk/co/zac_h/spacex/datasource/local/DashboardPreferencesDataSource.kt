package uk.co.zac_h.spacex.datasource.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import uk.co.zac_h.spacex.DASHBOARD_PREFERENCES
import javax.inject.Inject

class DashboardPreferencesDataSource @Inject constructor(
    @ApplicationContext context: Context
) : LocalSharedPreferences<Boolean>(context, DASHBOARD_PREFERENCES) {

    override fun getValue(key: String) = preferences.getBoolean(key, false)

    override fun add(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    override fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }
}