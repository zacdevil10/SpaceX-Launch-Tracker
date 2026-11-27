package uk.co.zac_h.spacex.network

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * CelesTrak refused a download (HTTP 403) because this IP already downloaded the group since its
 * data last changed. CelesTrak updates GP data every 2 hours, and blocks IPs that keep asking
 * after a 403, so nothing is requested again before [retryAtMillis].
 */
class CelestrakRateLimitedException(val retryAtMillis: Long) :
    Exception("CelesTrak allows one download of this group every 2 hours")

/**
 * When to try again after CelesTrak's 403 [body], e.g. "GP data has not updated since your last
 * successful download of GROUP=starlink at 2026-11-25 23:49:34 UTC. Data is updated once every 2
 * hours.": 2 hours after that last download, or 2 hours after [now] if the body can't be read.
 */
internal fun celestrakRetryAt(body: String?, now: Long): Long {
    val lastDownload = body
        ?.let { LAST_DOWNLOAD.find(it)?.groupValues?.get(1) }
        ?.let { runCatching { LocalDateTime.parse(it, LAST_DOWNLOAD_FORMAT).toInstant(ZoneOffset.UTC).toEpochMilli() }.getOrNull() }
    return (lastDownload ?: now) + CELESTRAK_UPDATE_INTERVAL_MILLIS
}

private const val CELESTRAK_UPDATE_INTERVAL_MILLIS = 2 * 60 * 60 * 1000L
private val LAST_DOWNLOAD = Regex("""at (\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}) UTC""")
private val LAST_DOWNLOAD_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
