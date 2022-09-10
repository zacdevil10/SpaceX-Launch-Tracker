package uk.co.zac_h.spacex.dto.spacex

import com.squareup.moshi.Json

data class NetworkDocsResponse<T>(
    @field:Json(name = "docs") val docs: List<T>
)
