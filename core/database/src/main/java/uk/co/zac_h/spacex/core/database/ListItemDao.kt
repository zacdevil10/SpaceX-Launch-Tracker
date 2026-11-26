package uk.co.zac_h.spacex.core.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert

@Dao
interface ListItemDao {

    /** The items of [list] in the API's order, read page by page as the list scrolls. */
    @Query("SELECT * FROM list_items WHERE list = :list ORDER BY position")
    fun pagingSource(list: String): PagingSource<Int, ListItemEntity>

    /** How many items of [list] are stored: the offset of the next page to fetch. */
    @Query("SELECT COUNT(*) FROM list_items WHERE list = :list")
    suspend fun count(list: String): Int

    @Upsert
    suspend fun upsertAll(items: List<ListItemEntity>)

    @Query("DELETE FROM list_items WHERE list = :list")
    suspend fun deleteList(list: String)

    /** Replaces every stored item of [list] with [items], its freshly fetched first page. */
    @Transaction
    suspend fun replaceList(list: String, items: List<ListItemEntity>) {
        deleteList(list)
        upsertAll(items)
    }
}
