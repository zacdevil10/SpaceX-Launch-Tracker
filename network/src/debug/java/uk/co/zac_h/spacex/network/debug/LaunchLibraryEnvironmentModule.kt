package uk.co.zac_h.spacex.network.debug

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.co.zac_h.spacex.network.LaunchLibraryEnvironmentSource

/** Debug builds call whichever server is picked in the debug tools. */
@Module
@InstallIn(SingletonComponent::class)
abstract class LaunchLibraryEnvironmentModule {

    @Binds
    abstract fun bindsLaunchLibraryEnvironmentSource(settings: DebugSettings): LaunchLibraryEnvironmentSource
}
