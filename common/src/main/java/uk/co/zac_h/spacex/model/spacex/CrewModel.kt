package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class CrewDocsModel(
    @field:Json(name = "docs") val docs: List<CrewModel>
)

@Parcelize
data class CrewModel(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "agency") val agency: String,
    @field:Json(name = "image") val image: String,
    @field:Json(name = "wikipedia") val wikipedia: String,
    @field:Json(name = "launches") val launches: List<CrewLaunchesModel>,
    @field:Json(name = "status") val status: String,
    @field:Json(name = "id") val id: String
) : Parcelable

@Parcelize
data class CrewLaunchesModel(
    @field:Json(name = "flight_number") val flightNumber: Int,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "id") val id: String
) : Parcelable