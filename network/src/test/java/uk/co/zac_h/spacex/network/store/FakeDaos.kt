package uk.co.zac_h.spacex.network.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import uk.co.zac_h.spacex.core.database.FetchMetadataDao
import uk.co.zac_h.spacex.core.database.FetchMetadataEntity
import uk.co.zac_h.spacex.core.database.LaunchDao
import uk.co.zac_h.spacex.core.database.LaunchEntity
import uk.co.zac_h.spacex.core.database.StarlinkDao
import uk.co.zac_h.spacex.core.database.StarlinkSatelliteEntity

/** In-memory stand-ins for the Room DAOs, so the store logic runs as a plain JVM test. */
class FakeLaunchDao : LaunchDao {
    val rows = MutableStateFlow<Map<String, LaunchEntity>>(emptyMap())

    override fun observeUpcoming(): Flow<List<LaunchEntity>> = rows.map { all ->
        all.values.filter { it.upcoming }.sortedWith(compareBy(nullsLast()) { it.net })
    }

    override suspend fun getUpcoming(): List<LaunchEntity> = rows.value.values.filter { it.upcoming }

    override suspend fun upsertAll(launches: List<LaunchEntity>) {
        rows.value = rows.value + launches.associateBy { it.id }
    }

    override suspend fun deleteByIds(ids: List<String>) {
        rows.value = rows.value - ids.toSet()
    }
}

class FakeFetchMetadataDao : FetchMetadataDao {
    val rows = MutableStateFlow<Map<String, FetchMetadataEntity>>(emptyMap())

    override fun observe(key: String): Flow<FetchMetadataEntity?> = rows.map { it[key] }

    override suspend fun get(key: String): FetchMetadataEntity? = rows.value[key]

    override suspend fun upsert(metadata: FetchMetadataEntity) {
        rows.value = rows.value + (metadata.key to metadata)
    }
}

class FakeStarlinkDao : StarlinkDao {
    val rows = MutableStateFlow<List<StarlinkSatelliteEntity>>(emptyList())

    override fun observeAll(): Flow<List<StarlinkSatelliteEntity>> = rows

    override suspend fun insertAll(satellites: List<StarlinkSatelliteEntity>) {
        rows.value = rows.value + satellites
    }

    override suspend fun deleteAll() {
        rows.value = emptyList()
    }
}
