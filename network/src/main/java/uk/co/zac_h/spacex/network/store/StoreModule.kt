package uk.co.zac_h.spacex.network.store

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object StoreModule {

    @Provides
    fun providesClock(): Clock = Clock(System::currentTimeMillis)
}
