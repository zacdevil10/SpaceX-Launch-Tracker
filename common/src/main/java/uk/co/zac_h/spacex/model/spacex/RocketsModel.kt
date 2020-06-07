package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RocketsModel(
    @field:Json(name = "height") val height: DimensModel?,
    @field:Json(name = "diameter") val diameter: DimensModel?,
    @field:Json(name = "mass") val mass: MassModel?,
    @field:Json(name = "first_stage") val firstStage: FirstStageModel?,
    @field:Json(name = "second_stage") val secondStage: SecondStageModel?,
    @field:Json(name = "engines") val engines: EngineConfigModel?,
    @field:Json(name = "landing_legs") val landingLegs: LandingLegsModel?,
    @field:Json(name = "payload_weights") val payloadWeights: List<PayloadWeightsModel>,
    @field:Json(name = "flickr_images") val flickr: List<String>,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "type") val type: String?,
    @field:Json(name = "active") val isActive: Boolean?,
    @field:Json(name = "stages") val stages: Int?,
    @field:Json(name = "boosters") val boosters: Int?,
    @field:Json(name = "cost_per_launch") val costPerLaunch: Int?,
    @field:Json(name = "success_rate_pct") val successRate: Int?,
    @field:Json(name = "first_flight") val firstFlight: String?,
    @field:Json(name = "country") val country: String?,
    @field:Json(name = "company") val company: String?,
    @field:Json(name = "wikipedia") val wikipedia: String?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "id") val id: String
) : Parcelable

