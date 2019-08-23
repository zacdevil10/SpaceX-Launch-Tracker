package uk.co.zac_h.spacex.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.format(): String =
    SimpleDateFormat("dd MMM yy - HH:mm zzz", Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }.format(Date(this.times(1000L)))