package uk.co.zac_h.spacex.utils.data

import com.squareup.moshi.Json

data class LaunchConfigModel(
    @field:Json(name = "rocket_id") var id: String
)