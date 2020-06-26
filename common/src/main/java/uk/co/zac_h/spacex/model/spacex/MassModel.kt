package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
@Parcelize
data class MassModel(
    @field:Json(name = "kg") var kg: Int?,
    @field:Json(name = "lb") var lb: Int?
) : Parcelable