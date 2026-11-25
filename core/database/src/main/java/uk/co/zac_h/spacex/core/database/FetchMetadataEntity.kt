package uk.co.zac_h.spacex.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** When the list stored under [key] was last fetched, in milliseconds since the epoch. */
@Entity(tableName = "fetch_metadata")
data class FetchMetadataEntity(
    @PrimaryKey val key: String,
    @ColumnInfo(name = "fetched_at") val fetchedAt: Long
)
