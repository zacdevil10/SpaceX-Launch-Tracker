package uk.co.zac_h.spacex.rest

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.co.zac_h.spacex.utils.SPACEX_BASE_URL
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
class SpaceXNetworkModule {

    @Provides
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(SPACEX_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    fun providesHttpService(retrofit: Retrofit): SpaceXService = retrofit.create(SpaceXService::class.java)

}