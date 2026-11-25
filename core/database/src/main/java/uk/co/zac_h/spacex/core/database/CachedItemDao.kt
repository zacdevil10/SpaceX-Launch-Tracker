package uk.co.zac_h.spacex.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedItemDao {

    @Query("SELECT * FROM cached_items WHERE list_key = :listKey ORDER BY position")
    fun observe(listKey: String): Flow<List<CachedItemEntity>>

    @Query("DELETE FROM cached_items WHERE list_key = :listKey")
    suspend fun deleteList(listKey: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CachedItemEntity>)

    /** Replaces everything stored under [listKey] with [items], in one transaction. */
    @Transaction
    suspend fun replaceList(listKey: String, items: List<CachedItemEntity>) {
        deleteList(listKey)
        insertAll(items)
    }
}
