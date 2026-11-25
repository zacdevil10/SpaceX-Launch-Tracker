package uk.co.zac_h.spacex.core.database

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

    @Upsert
    suspend fun upsertAll(launches: List<LaunchEntity>)

    @Query("DELETE FROM launches WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)

    /**
     * Stores [launches] as the complete set of upcoming launches.
     *
     * A stored upcoming launch missing from [launches] has either completed (it is kept and moved
     * to previous launches) or, if its time is still in the future, was removed from the API (it is
     * deleted).
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
