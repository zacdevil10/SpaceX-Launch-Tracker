package uk.co.zac_h.spacex.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface StarlinkDao {

    @Query("SELECT * FROM starlink_satellites")
    fun observeAll(): Flow<List<StarlinkSatelliteEntity>>

    @Insert
    suspend fun insertAll(satellites: List<StarlinkSatelliteEntity>)

    @Query("DELETE FROM starlink_satellites")
    suspend fun deleteAll()

    /**
     * Replaces every stored satellite with [satellites], the complete Starlink group. Satellites
     * that have re-entered drop out of the group, so they are removed here too.
     */
    @Transaction
    suspend fun replaceAll(satellites: List<StarlinkSatelliteEntity>) {
        deleteAll()
        insertAll(satellites)
    }
}
