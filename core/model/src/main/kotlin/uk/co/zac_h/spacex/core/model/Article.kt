package uk.co.zac_h.spacex.core.model

import java.time.Instant

/** A Spaceflight News article. */
data class Article(
    val id: Int,
    val title: String,
    val url: String,
    val imageUrl: String,
    val site: String,
    val summary: String,
    val published: Instant?,
    val updated: Instant?,
)
