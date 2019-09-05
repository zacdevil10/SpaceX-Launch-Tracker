package uk.co.zac_h.spacex.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.format(tbd: Boolean = false): String =
    SimpleDateFormat(
        if (!tbd) "dd MMM yy - HH:mm zzz" else "MMM yy - HH:mm zzz",
        Locale.getDefault()
    ).apply {
        timeZone = TimeZone.getDefault()
    }.format(Date(this.times(1000L)))