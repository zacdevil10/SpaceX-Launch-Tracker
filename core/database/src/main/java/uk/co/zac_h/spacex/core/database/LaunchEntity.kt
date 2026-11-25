package uk.co.zac_h.spacex.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * One launch, stored once whichever list it appears in. Upcoming and previous launches are queries
 * on this table, so a launch that completes moves between them without being fetched twice.
 *
 * [json] is the launch exactly as the API model encodes it; the other columns are copied out of it
 * so they can be queried and sorted.
 */
@Entity(
    tableName = "launches",
    indices = [Index(value = ["upcoming", "net"]), Index(value = ["last_updated"])]
)
data class LaunchEntity(
    @PrimaryKey val id: String,
    /** Launch time (NET) in milliseconds since the epoch, or null if the API has none. */
    val net: Long?,
    /** True if the launch came from the upcoming endpoint, false if from the previous endpoint. */
    val upcoming: Boolean,
    /** When Launch Library last changed this launch, in milliseconds since the epoch. */
    @ColumnInfo(name = "last_updated") val lastUpdated: Long?,
    val json: String
)
