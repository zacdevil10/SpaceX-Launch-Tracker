package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

//v4

@Parcelize
data class CoreModel(
    @field:Json(name = "serial") val serial: String,
    @field:Json(name = "block") val block: String?,
    @field:Json(name = "status") val status: String,
    @field:Json(name = "reuse_count") val reuseCount: Int,
    @field:Json(name = "rtls_attempts") val attemptsRtls: Int,
    @field:Json(name = "rtls_landings") val landingsRtls: Int,
    @field:Json(name = "asds_attempts") val attemptsAsds: Int,
    @field:Json(name = "asds_landings") val landingsAsds: Int,
    @field:Json(name = "last_update") val lastUpdate: String?,
    @field:Json(name = "launches") val missions: List<String>,
    @field:Json(name = "id") val id: String
) : Parcelable

data class CoreDocsModel(
    @field:Json(name = "docs") val docs: List<CoreExtendedModel>
)

@Parcelize
data class CoreExtendedModel(
    @field:Json(name = "serial") val serial: String,
    @field:Json(name = "block") val block: String?,
    @field:Json(name = "status") val status: String,
    @field:Json(name = "reuse_count") val reuseCount: Int,
    @field:Json(name = "rtls_attempts") val attemptsRtls: Int,
    @field:Json(name = "rtls_landings") val landingsRtls: Int,
    @field:Json(name = "asds_attempts") val attemptsAsds: Int,
    @field:Json(name = "asds_landings") val landingsAsds: Int,
    @field:Json(name = "last_update") val lastUpdate: String?,
    @field:Json(name = "launches") val missions: List<MissionsModel>?,
    @field:Json(name = "id") val id: String
) : Parcelable

