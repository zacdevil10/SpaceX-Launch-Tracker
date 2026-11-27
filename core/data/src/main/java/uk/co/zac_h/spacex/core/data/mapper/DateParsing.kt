package uk.co.zac_h.spacex.core.data.mapper

import java.time.Instant
import java.time.OffsetDateTime

/** A Launch Library date-time, such as "2026-11-29T04:39:00Z", or null if missing or malformed. */
internal fun String?.toInstantOrNull(): Instant? =
    this?.let { runCatching { OffsetDateTime.parse(it).toInstant() }.getOrNull() }
