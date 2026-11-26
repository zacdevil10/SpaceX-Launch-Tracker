package uk.co.zac_h.spacex.core.database

import androidx.room.Entity
import androidx.room.Index

/**
 * One item of a paged Launch Library list that has no fields worth querying (launchers,
 * spacecraft, astronauts). [list] names the list, [position] keeps the API's order, and [json] is
 * the item exactly as Launch Library returned it.
 */
@Entity(
    tableName = "list_items",
    primaryKeys = ["list", "id"],
    indices = [Index("list", "position")]
)
data class ListItemEntity(
    val list: String,
    val id: String,
    val position: Int,
    val json: String
)
