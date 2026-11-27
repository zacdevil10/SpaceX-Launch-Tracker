package uk.co.zac_h.spacex.network.dto.celestrak

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * One satellite's general perturbations (GP) mean elements, in CelesTrak's OMM JSON format. Only
 * the fields SGP4 needs are read.
 */
@Serializable
data class GpElementDto(
    @SerialName("OBJECT_NAME") val objectName: String,
    @SerialName("NORAD_CAT_ID") val noradCatId: Int,
    /** UTC, without a zone, e.g. `2026-11-25T05:46:04.143168`. */
    @SerialName("EPOCH") val epoch: String,
    @SerialName("MEAN_MOTION") val meanMotion: Double,
    @SerialName("ECCENTRICITY") val eccentricity: Double,
    @SerialName("INCLINATION") val inclination: Double,
    @SerialName("RA_OF_ASC_NODE") val raOfAscNode: Double,
    @SerialName("ARG_OF_PERICENTER") val argOfPericenter: Double,
    @SerialName("MEAN_ANOMALY") val meanAnomaly: Double,
    @SerialName("BSTAR") val bstar: Double
)
