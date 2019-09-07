package uk.co.zac_h.spacex.utils.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LaunchSiteModel(
    @field:Json(name = "site_id") var id: String,
    @field:Json(name = "site_name") var name: String,
    @field:Json(name = "site_name_long") var nameLong: String
) : Parcelable