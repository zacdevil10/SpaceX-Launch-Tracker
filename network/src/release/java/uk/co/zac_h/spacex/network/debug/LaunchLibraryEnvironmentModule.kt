package uk.co.zac_h.spacex.network.debug

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.co.zac_h.spacex.network.LaunchLibraryEnvironment
import uk.co.zac_h.spacex.network.LaunchLibraryEnvironmentSource

/** Release builds have no debug tools and always call the production server. */
@Module
@InstallIn(SingletonComponent::class)
object LaunchLibraryEnvironmentModule {

    @Provides
    fun providesLaunchLibraryEnvironmentSource(): LaunchLibraryEnvironmentSource =
        LaunchLibraryEnvironmentSource { LaunchLibraryEnvironment.PRODUCTION }
}
