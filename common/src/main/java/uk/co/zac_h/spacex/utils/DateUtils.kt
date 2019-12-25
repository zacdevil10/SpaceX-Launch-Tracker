package uk.co.zac_h.spacex.utils

import java.text.SimpleDateFormat
import java.util.*

private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS

fun Long.formatDateMillisLong(tbd: Boolean = false): String =
    SimpleDateFormat(
        if (!tbd) "dd MMM yyyy - HH:mm zzz" else "MMM yyyy - HH:mm zzz",
        Locale.getDefault()
    ).apply {
        timeZone = TimeZone.getDefault()
    }.format(Date(this.times(1000L)))

fun Long.formatDateMillisShort(tbd: Boolean = false): String =
    SimpleDateFormat(
        if (!tbd) "dd MMM yy - HH:mm" else "MMM yyyy - HH:mm",
        Locale.getDefault()
    ).apply {
        timeZone = TimeZone.getDefault()
    }.format(Date(this.times(1000L)))

fun Long.formatDateMillisDDMMM(): String =
    SimpleDateFormat(
        "dd MMM",
        Locale.getDefault()
    ).apply {
        timeZone = TimeZone.getDefault()
    }.format(Date(this.times(1000L)))

fun String.formatDateString(): Date? {
    val formatInput = SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")

    return formatInput.parse(this)
}

fun String.dateStringToMillis(): Long = this.formatDateString()?.time ?: 0

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

    val lessThanSevenDays = SimpleDateFormat("dd MMM", Locale.getDefault())
    val moreThanSevenDays = SimpleDateFormat("dd MMM yy", Locale.getDefault())

    return getString(
        dateMilli,
        currentTime,
        date,
        currentDate,
        lessThanSevenDays,
        moreThanSevenDays
    )
}

private fun getString(
    dateMilli: Long?,
    currentTime: Long,
    date: Date,
    currentDate: Date,
    lessThanSevenDays: SimpleDateFormat,
    moreThanSevenDays: SimpleDateFormat
): String {
    val diff = currentTime - dateMilli!!
    when {
        diff < 50 * MINUTE_MILLIS -> return (diff / MINUTE_MILLIS).toString() + "m"
        diff < 24 * HOUR_MILLIS -> return (diff / HOUR_MILLIS).toString() + "h"
        diff < 48 * HOUR_MILLIS -> return "1d"
        else -> {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DATE, 7)

            val formattedDate: String
            formattedDate = if (calendar.time < currentDate) {
                moreThanSevenDays.format(date)
            } else {
                lessThanSevenDays.format(date)
            }

            return formattedDate
        }
    }
}
