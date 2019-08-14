package uk.co.zac_h.spacex.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long?.format(): String {
    val dateF = this?.times(1000L)?.let { Date(it) }
    val dateFormat = SimpleDateFormat("dd MMM yy - HH:mm zzz", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getDefault()

    return dateFormat.format(dateF)
}