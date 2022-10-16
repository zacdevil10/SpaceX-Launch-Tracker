package uk.co.zac_h.spacex.core.utils

import uk.co.zac_h.spacex.core.types.DatePrecision
import java.text.SimpleDateFormat
import java.util.*

private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS

fun String.formatDate(): String {
    val date = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss",
        Locale.ENGLISH
    ).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.parse(this)

    return SimpleDateFormat(
        "dd MMM yy - HH:mm zzz",
        Locale.ENGLISH
    ).apply {
        timeZone = TimeZone.getDefault()
    }.format(date)
}

fun String.toMillis(): Long? = SimpleDateFormat(
    "yyyy-MM-dd'T'HH:mm:ss",
    Locale.ENGLISH
).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}.parse(this)?.time

fun Long.formatDateMillisLong(
    precision: DatePrecision? = null
): String =
    SimpleDateFormat(
        precision?.precision ?: "dd MMM yy - HH:mm zzz",
        Locale.ENGLISH
    ).apply {
        timeZone = TimeZone.getDefault()
    }.format(Date(this.times(1000L)))

fun Long.formatDateMillisShort(tbd: Boolean = false): String =
    SimpleDateFormat(
        if (!tbd) "dd MMM yy - HH:mm" else "MMM yy",
        Locale.ENGLISH
    ).apply {
        timeZone = TimeZone.getDefault()
    }.format(Date(this.times(1000L)))

fun Long.formatDateMillisDDMMM(): String =
    SimpleDateFormat(
        "dd MMM",
        Locale.ENGLISH
    ).apply {
        timeZone = TimeZone.getDefault()
    }.format(Date(this.times(1000L)))

fun Long.formatDateMillisYYYY(): Int =
    SimpleDateFormat("yyyy", Locale.ENGLISH).apply {
        timeZone = TimeZone.getDefault()
    }.format(Date(this.times(1000L))).toInt()

fun String.dateStringToMillis(): Long? =
    try {
        SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH).parse(this)?.time
    } catch (e: Exception) {
        0
    }

fun Long.formatRange(): String =
    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(this))

fun Long.convertDate(): String {
    var dateMilli = this
    if (dateMilli < 1000000000000L) {
        //Timestamp is in seconds
        dateMilli *= 1000
    }

    val currentTime = System.currentTimeMillis()
    if (dateMilli > currentTime || dateMilli <= 0) {
        return ""
    }

    val date = Date(dateMilli)
    val currentDate = Date(currentTime)

    return getString(
        dateMilli,
        currentTime,
        date,
        currentDate
    )
}

fun getString(
    dateMilli: Long?,
    currentTime: Long,
    date: Date,
    currentDate: Date
): String {
    if (dateMilli == null) return ""

    val lessThanSevenDays = SimpleDateFormat("dd MMM", Locale.ENGLISH)
    val moreThanSevenDays = SimpleDateFormat("dd MMM yy", Locale.ENGLISH)

    val diff = currentTime - dateMilli
    when {
        diff < 50 * MINUTE_MILLIS -> return (diff / MINUTE_MILLIS).toString() + "m"
        diff < 24 * HOUR_MILLIS -> return (diff / HOUR_MILLIS).toString() + "h"
        diff < 48 * HOUR_MILLIS -> return "1d"
        else -> {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DATE, 7)

            val formattedDate: String = if (calendar.time < currentDate) {
                moreThanSevenDays.format(date)
            } else {
                lessThanSevenDays.format(date)
            }

            return formattedDate
        }
    }
}

data class EventDate(
    val dateUtc: String? = null,
    val dateUnix: Long? = null,
    val dateLocal: String? = null
)
