package uk.co.zac_h.spacex.utils

import uk.co.zac_h.spacex.model.spacex.MassFormatted

fun Float.add(
    f1: Float? = null,
    f9: Float? = null,
    fh: Float? = null
) = this.plus(f1 ?: 0f).plus(f9 ?: 0f).plus(fh ?: 0f)

fun formatMass(massKg: Float?, massLbs: Float?): MassFormatted? =
    if (massKg != null && massLbs != null) {
        MassFormatted(kg = massKg.metricFormat(), lb = massLbs.metricFormat())
    } else null