package uk.co.zac_h.spacex.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One Starlink satellite's mean orbital elements (an OMM / GP record from CelesTrak). Positions are
 * worked out from these on the device with SGP4, so they only need fetching about once a day.
 */
@Entity(tableName = "starlink_satellites")
data class StarlinkSatelliteEntity(
    @PrimaryKey @ColumnInfo(name = "norad_id") val noradId: Int,
    val name: String,
    /** Epoch of the elements, in milliseconds since the epoch (UTC). */
    val epoch: Long,
    /** Revolutions per day. */
    @ColumnInfo(name = "mean_motion") val meanMotion: Double,
    val eccentricity: Double,
    /** Degrees. */
    val inclination: Double,
    /** Right ascension of the ascending node, in degrees. */
    @ColumnInfo(name = "ra_of_asc_node") val raOfAscNode: Double,
    /** Argument of perigee, in degrees. */
    @ColumnInfo(name = "arg_of_pericenter") val argOfPericenter: Double,
    /** Degrees. */
    @ColumnInfo(name = "mean_anomaly") val meanAnomaly: Double,
    /** SGP4 drag term, in inverse Earth radii. */
    val bstar: Double
)
