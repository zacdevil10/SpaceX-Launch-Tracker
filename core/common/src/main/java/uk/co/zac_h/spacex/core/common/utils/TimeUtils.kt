package uk.co.zac_h.spacex.core.common.utils

import java.util.concurrent.TimeUnit

private fun Long.toDays() = TimeUnit.MILLISECONDS.toDays(this)

private fun Long.toHours() = TimeUnit.MILLISECONDS.toHours(this)

private fun Long.toMinutes() = TimeUnit.MILLISECONDS.toMinutes(this)

private fun Long.toSeconds() = TimeUnit.MILLISECONDS.toSeconds(this)

fun Long.toCountdownDays() = this.toDays()

fun Long.toCountdownHours() = this.toHours() - TimeUnit.DAYS.toHours(this.toDays())

fun Long.toCountdownMinutes() = this.toMinutes() - TimeUnit.HOURS.toMinutes(this.toHours())

fun Long.toCountdownSeconds() = this.toSeconds() - TimeUnit.MINUTES.toSeconds(this.toMinutes())
