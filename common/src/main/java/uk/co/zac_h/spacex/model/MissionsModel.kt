package uk.co.zac_h.spacex.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MissionsModel(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "flight") val flightNumber: Int?
) : Parcelable