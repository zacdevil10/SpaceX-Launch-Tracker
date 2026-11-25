package uk.co.zac_h.spacex.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

/**
 * One item of a list fetched from an API, stored as the JSON the app decodes it from.
 *
 * Every list (upcoming launches, a page of previous launches, launchers...) shares this table,
 * keyed by [listKey] and ordered by [position], so a new list needs no schema change. Storing
 * JSON keeps the API models as the only description of the data.
 */
@Entity(
    tableName = "cached_items",
    primaryKeys = ["list_key", "item_id"],
    indices = [Index(value = ["list_key", "position"])]
)
data class CachedItemEntity(
    @ColumnInfo(name = "list_key") val listKey: String,
    @ColumnInfo(name = "item_id") val itemId: String,
    val position: Int,
    val json: String
)
