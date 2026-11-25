package uk.co.zac_h.spacex.core.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FetchMetadataDao {

    @Query("SELECT * FROM fetch_metadata WHERE `key` = :key")
    fun observe(key: String): Flow<FetchMetadataEntity?>

    @Query("SELECT * FROM fetch_metadata WHERE `key` = :key")
    suspend fun get(key: String): FetchMetadataEntity?

    @Upsert
    suspend fun upsert(metadata: FetchMetadataEntity)
}
