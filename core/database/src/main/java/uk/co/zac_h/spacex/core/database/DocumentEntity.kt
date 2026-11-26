package uk.co.zac_h.spacex.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/** A single Launch Library response stored whole, such as the SpaceX agency, by [key]. */
@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey val key: String,
    val json: String
)
