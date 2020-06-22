package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
@Parcelize
data class LaunchCoreModel(
    @field:Json(name = "core") var id: String?,
    @field:Json(name = "flight") var flight: Int?,
    @field:Json(name = "gridfins") var gridfins: Boolean?,
    @field:Json(name = "legs") var legs: Boolean?,
    @field:Json(name = "reused") var reused: Boolean?,
    @field:Json(name = "landing_attempt") var landingAttempt: Boolean?,
    @field:Json(name = "landing_success") var landingSuccess: Boolean?,
    @field:Json(name = "landing_type") var landingType: String?,
    @field:Json(name = "landpad") var landingPad: String?
) : Parcelable

@Parcelize
data class LaunchCoreExtendedModel(
    @field:Json(name = "core") var core: CoreExtendedModel?,
    @field:Json(name = "flight") var flight: Int?,
    @field:Json(name = "gridfins") var gridfins: Boolean?,
    @field:Json(name = "legs") var legs: Boolean?,
    @field:Json(name = "reused") var reused: Boolean?,
    @field:Json(name = "landing_attempt") var landingAttempt: Boolean?,
    @field:Json(name = "landing_success") var landingSuccess: Boolean?,
    @field:Json(name = "landing_type") var landingType: String?,
    @field:Json(name = "landpad") var landingPad: LandingPadModel?
) : Parcelable