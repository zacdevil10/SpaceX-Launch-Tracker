package uk.co.zac_h.spacex.core.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {

    @Query("SELECT * FROM documents WHERE `key` = :key")
    fun observe(key: String): Flow<DocumentEntity?>

    @Upsert
    suspend fun upsert(document: DocumentEntity)
}
