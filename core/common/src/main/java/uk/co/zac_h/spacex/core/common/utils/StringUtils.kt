package uk.co.zac_h.spacex.core.common.utils

import java.text.DecimalFormat

fun Any.metricFormat(): String = DecimalFormat("#,###.##").format(this)

fun String?.orUnknown(): String = this ?: "Unknown"
