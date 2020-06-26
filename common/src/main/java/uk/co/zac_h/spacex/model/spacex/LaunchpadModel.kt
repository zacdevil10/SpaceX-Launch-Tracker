package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4

@Parcelize
data class LaunchpadDocsModel(
    @field:Json(name = "docs") val docs: List<LaunchpadModel>
) : Parcelable

@Parcelize
data class LaunchpadModel(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "full_name") val fullName: String?,
    @field:Json(name = "status") val status: String,
    @field:Json(name = "locality") val locality: String?,
    @field:Json(name = "region") val region: String?,
    @field:Json(name = "timezone") val timezone: String?,
    @field:Json(name = "latitude") val lat: Float?,
    @field:Json(name = "longitude") val lng: Float?,
    @field:Json(name = "launch_attempts") val launchAttempts: Int,
    @field:Json(name = "launch_successes") val launchSuccesses: Int,
    @field:Json(name = "rockets") val rockets: List<String>,
    @field:Json(name = "launches") val launches: List<String>,
    @field:Json(name = "id") var id: String
) : Parcelable

