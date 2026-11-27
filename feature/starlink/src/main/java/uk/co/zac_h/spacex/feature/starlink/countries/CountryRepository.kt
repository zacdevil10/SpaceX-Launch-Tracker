package uk.co.zac_h.spacex.feature.starlink.countries

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import uk.co.zac_h.spacex.feature.starlink.R
import javax.inject.Inject
import javax.inject.Singleton

/** Loads the bundled country borders once, the first time they're needed. */
@Singleton
class CountryRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val mutex = Mutex()
    private var lookup: CountryLookup? = null

    suspend fun lookup(): CountryLookup = mutex.withLock {
        lookup ?: withContext(Dispatchers.IO) {
            val json = context.resources.openRawResource(R.raw.starlink_countries)
                .bufferedReader().use { it.readText() }
            CountryLookup.parse(json)
        }.also { lookup = it }
    }
}
