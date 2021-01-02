package uk.co.zac_h.spacex.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.utils.repo.DashboardPreferencesRepository
import uk.co.zac_h.spacex.utils.repo.DashboardSharedPreferencesService
import uk.co.zac_h.spacex.utils.repo.PinnedPreferencesRepository
import uk.co.zac_h.spacex.utils.repo.PinnedPreferencesService

@Module
@InstallIn(ActivityComponent::class)
class SharedPreferencesModule {

    @Provides
    fun providesDashboardSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(App.DASHBOARD_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    fun providesPinnedSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(App.PINNED_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    fun providesDashboardSharedPreferencesService(sharedPreferences: SharedPreferences): DashboardSharedPreferencesService =
        DashboardPreferencesRepository(sharedPreferences)

    @Provides
    fun providesPinnedPreferencesService(sharedPreferences: SharedPreferences): PinnedPreferencesService =
        PinnedPreferencesRepository(sharedPreferences)

}