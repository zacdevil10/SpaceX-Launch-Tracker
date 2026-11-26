package uk.co.zac_h.spacex.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The app's source of truth for Launch Library data and Starlink orbital elements. Screens read
 * from here; the network only fills it. Launch Library allows 15 calls per hour per IP, so data is
 * refetched only once it is older than its window.
 *
 * Bump [version] whenever an entity changes. Room refuses to open a database whose schema doesn't
 * match its version, and a new version just recreates the tables (see DatabaseModule).
 */
@Database(
    entities = [
        LaunchEntity::class,
        ListItemEntity::class,
        DocumentEntity::class,
        FetchMetadataEntity::class,
        StarlinkSatelliteEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class SpaceXDatabase : RoomDatabase() {

    abstract fun launchDao(): LaunchDao

    abstract fun listItemDao(): ListItemDao

    abstract fun documentDao(): DocumentDao

    abstract fun fetchMetadataDao(): FetchMetadataDao

    abstract fun starlinkDao(): StarlinkDao
}
