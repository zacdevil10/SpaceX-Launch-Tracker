package uk.co.zac_h.spacex.network.store

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import uk.co.zac_h.spacex.core.database.DocumentDao
import uk.co.zac_h.spacex.core.database.DocumentEntity
import uk.co.zac_h.spacex.core.database.FetchMetadataDao
import uk.co.zac_h.spacex.core.database.FetchMetadataEntity
import uk.co.zac_h.spacex.core.database.LaunchDao
import uk.co.zac_h.spacex.core.database.LaunchEntity
import uk.co.zac_h.spacex.core.database.ListItemDao
import uk.co.zac_h.spacex.core.database.ListItemEntity

/** In-memory stand-ins for the Room DAOs, so the store logic runs as a plain JVM test. */
class FakeLaunchDao : LaunchDao {
    val rows = MutableStateFlow<Map<String, LaunchEntity>>(emptyMap())

    override fun observeUpcoming(): Flow<List<LaunchEntity>> = rows.map { all ->
        all.values.filter { it.upcoming }.sortedWith(compareBy(nullsLast()) { it.net })
    }

    override suspend fun getUpcoming(): List<LaunchEntity> = rows.value.values.filter { it.upcoming }

    override fun observeById(id: String): Flow<LaunchEntity?> = rows.map { it[id] }

    override suspend fun upsertAll(launches: List<LaunchEntity>) {
        rows.value = rows.value + launches.associateBy { it.id }
    }

    override suspend fun deleteByIds(ids: List<String>) {
        rows.value = rows.value - ids.toSet()
    }

    override fun previousPagingSource(): PagingSource<Int, LaunchEntity> =
        error("Paging reads are Room's job; tests read rows directly")

    override suspend fun oldestPreviousNet(): Long? =
        rows.value.values.filter { !it.upcoming }.mapNotNull { it.net }.minOrNull()

    override suspend fun deletePrevious() {
        rows.value = rows.value.filterValues { it.upcoming }
    }

    /** Previous launch ids, most recent first, as the list would show them. */
    fun previousIds(): List<String> = rows.value.values.filter { !it.upcoming }
        .sortedByDescending { it.net }.map { it.id }
}

class FakeFetchMetadataDao : FetchMetadataDao {
    val rows = MutableStateFlow<Map<String, FetchMetadataEntity>>(emptyMap())

    override fun observe(key: String): Flow<FetchMetadataEntity?> = rows.map { it[key] }

    override suspend fun get(key: String): FetchMetadataEntity? = rows.value[key]

    override suspend fun upsert(metadata: FetchMetadataEntity) {
        rows.value = rows.value + (metadata.key to metadata)
    }
}

class FakeListItemDao : ListItemDao {
    val rows = MutableStateFlow<Map<Pair<String, String>, ListItemEntity>>(emptyMap())

    override fun pagingSource(list: String): PagingSource<Int, ListItemEntity> =
        error("Paging reads are Room's job; tests read rows directly")

    override fun observe(list: String, id: String): Flow<ListItemEntity?> = rows.map { it[list to id] }

    override suspend fun count(list: String): Int = rows.value.keys.count { it.first == list }

    override suspend fun upsertAll(items: List<ListItemEntity>) {
        rows.value = rows.value + items.associateBy { it.list to it.id }
    }

    override suspend fun deleteList(list: String) {
        rows.value = rows.value.filterKeys { it.first != list }
    }

    /** The ids stored under [list], in the order the list shows them. */
    fun ids(list: String): List<String> =
        rows.value.values.filter { it.list == list }.sortedBy { it.position }.map { it.id }
}

class FakeDocumentDao : DocumentDao {
    val rows = MutableStateFlow<Map<String, DocumentEntity>>(emptyMap())

    override fun observe(key: String): Flow<DocumentEntity?> = rows.map { it[key] }

    override suspend fun upsert(document: DocumentEntity) {
        rows.value = rows.value + (document.key to document)
    }
}
