package uk.co.zac_h.spacex.feature.assets.vehicles

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

/** This instant in the device's time zone, such as "04 Jun 2010" for "dd MMM yyyy". */
internal fun Instant.formatLocal(pattern: String): String =
    DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH).withZone(ZoneId.systemDefault()).format(this)

/** Midnight UTC on this date, in milliseconds since the epoch, for sorting. */
internal fun LocalDate.toEpochMilliUtc(): Long =
    atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
