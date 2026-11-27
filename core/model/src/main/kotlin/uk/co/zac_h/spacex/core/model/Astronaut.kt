package uk.co.zac_h.spacex.core.model

import java.time.Instant

data class Astronaut(
    val id: Int,
    val name: String?,
    val status: CrewStatus,
    val agency: String?,
    val nationalities: List<String>,
    val bio: String?,
    val imageUrl: String?,
    val firstFlight: Instant?,
)
