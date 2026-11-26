package uk.co.zac_h.spacex.core.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LaunchDao {

    @Query("SELECT * FROM launches WHERE upcoming = 1 ORDER BY net IS NULL, net")
    fun observeUpcoming(): Flow<List<LaunchEntity>>

    @Query("SELECT * FROM launches WHERE upcoming = 1")
    suspend fun getUpcoming(): List<LaunchEntity>

    /** Previous launches, most recent first, read page by page as the list scrolls. */
    @Query("SELECT * FROM launches WHERE upcoming = 0 ORDER BY net IS NULL, net DESC")
    fun previousPagingSource(): PagingSource<Int, LaunchEntity>

    /** Launch time of the oldest stored previous launch: older history is fetched from there. */
    @Query("SELECT MIN(net) FROM launches WHERE upcoming = 0")
    suspend fun oldestPreviousNet(): Long?

    @Query("DELETE FROM launches WHERE upcoming = 0")
    suspend fun deletePrevious()

    @Upsert
    suspend fun upsertAll(launches: List<LaunchEntity>)

    @Query("DELETE FROM launches WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)

    /** Replaces every stored previous launch with [launches], e.g. after too long away to catch up. */
    @Transaction
    suspend fun replacePrevious(launches: List<LaunchEntity>) {
        deletePrevious()
        upsertAll(launches)
    }

    /**
     * Stores [launches], the complete response of the upcoming endpoint (each already marked
     * upcoming or not from its status).
     *
     * A stored upcoming launch missing from [launches] has either finished more than a day ago
     * (it is kept and moved to previous launches) or, if its time is still in the future, was
     * removed from the API (it is deleted).
     */
    @Transaction
    suspend fun replaceUpcoming(launches: List<LaunchEntity>, now: Long) {
        val newIds = launches.map { it.id }.toSet()
        val missing = getUpcoming().filter { it.id !in newIds }
        val (removed, completed) = missing.partition { (it.net ?: Long.MAX_VALUE) > now }
        deleteByIds(removed.map { it.id })
        upsertAll(completed.map { it.copy(upcoming = false) } + launches)
    }
}
