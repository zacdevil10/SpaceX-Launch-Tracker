package uk.co.zac_h.spacex.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The app's source of truth for Launch Library data. Screens read from here; the network only
 * fills it. Launch Library allows 15 calls per hour per IP, so data is refetched only once it is
 * older than its window.
 */
@Database(
    entities = [CachedItemEntity::class, FetchMetadataEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SpaceXDatabase : RoomDatabase() {

    abstract fun cachedItemDao(): CachedItemDao

    abstract fun fetchMetadataDao(): FetchMetadataDao
}
