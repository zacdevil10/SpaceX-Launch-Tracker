package uk.co.zac_h.spacex.network.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import uk.co.zac_h.spacex.core.database.CachedItemDao
import uk.co.zac_h.spacex.core.database.CachedItemEntity
import uk.co.zac_h.spacex.core.database.FetchMetadataDao
import uk.co.zac_h.spacex.core.database.FetchMetadataEntity

/** In-memory stand-ins for the Room DAOs, so the store logic runs as a plain JVM test. */
class FakeCachedItemDao : CachedItemDao {
    val rows = MutableStateFlow<List<CachedItemEntity>>(emptyList())

    override fun observe(listKey: String): Flow<List<CachedItemEntity>> =
        rows.map { all -> all.filter { it.listKey == listKey }.sortedBy { it.position } }

    override suspend fun deleteList(listKey: String) {
        rows.value = rows.value.filterNot { it.listKey == listKey }
    }

    override suspend fun insertAll(items: List<CachedItemEntity>) {
        val keys = items.map { it.listKey to it.itemId }.toSet()
        rows.value = rows.value.filterNot { (it.listKey to it.itemId) in keys } + items
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
