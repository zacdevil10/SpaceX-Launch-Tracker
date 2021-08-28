package uk.co.zac_h.spacex

import uk.co.zac_h.spacex.dto.spacex.MassFormatted
import uk.co.zac_h.spacex.utils.metricFormat

fun formatMass(massKg: Float?, massLbs: Float?): MassFormatted? =
    if (massKg != null && massLbs != null) {
        MassFormatted(kg = massKg.metricFormat(), lb = massLbs.metricFormat())
    } else null