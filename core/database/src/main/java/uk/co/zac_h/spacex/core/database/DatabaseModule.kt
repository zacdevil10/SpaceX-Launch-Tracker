package uk.co.zac_h.spacex.core.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): SpaceXDatabase =
        Room.databaseBuilder(context, SpaceXDatabase::class.java, "spacex.db")
            // Everything here can be fetched again, so a schema change just starts empty.
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun providesLaunchDao(database: SpaceXDatabase): LaunchDao = database.launchDao()

    @Provides
    fun providesListItemDao(database: SpaceXDatabase): ListItemDao = database.listItemDao()

    @Provides
    fun providesDocumentDao(database: SpaceXDatabase): DocumentDao = database.documentDao()

    @Provides
    fun providesFetchMetadataDao(database: SpaceXDatabase): FetchMetadataDao =
        database.fetchMetadataDao()
}
