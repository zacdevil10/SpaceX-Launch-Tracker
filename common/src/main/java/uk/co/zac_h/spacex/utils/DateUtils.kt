package uk.co.zac_h.spacex.utils

import java.text.SimpleDateFormat
import java.util.*

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

fun String.formatDateString(): String {
    val formatInput = SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
    val formatOutput = SimpleDateFormat("dd MMM")

    val date = formatInput.parse(this)

    return formatOutput.format(date)
}