package uk.co.zac_h.spacex

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.co.zac_h.spacex.rest.SpaceXInterface
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @SpaceXHttpClient
    fun providesSpaceXHttpClient(): SpaceXInterface = SpaceXInterface.create()

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SpaceXHttpClient