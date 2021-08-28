package uk.co.zac_h.spacex.utils

fun Float.add(
    f1: Float? = null,
    f9: Float? = null,
    fh: Float? = null
) = this.plus(f1 ?: 0f).plus(f9 ?: 0f).plus(fh ?: 0f)