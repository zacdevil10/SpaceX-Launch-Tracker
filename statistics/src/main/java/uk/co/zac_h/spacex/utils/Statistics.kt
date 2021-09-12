package uk.co.zac_h.spacex.utils

import androidx.annotation.StringRes
import uk.co.zac_h.spacex.statistics.R

enum class Statistics(@StringRes val title: Int, val image: Int) {
    LAUNCH_HISTORY(R.string.statistics_launch_history, R.drawable.ic_launch_history),
    LANDING_HISTORY(R.string.statistics_landing_history, R.drawable.ic_launch_history),
    LAUNCH_RATE(R.string.statistics_launch_rate, R.drawable.ic_launch_rate),
    MASS_TO_ORBIT(R.string.statistics_mass_to_orbit, R.drawable.ic_mass_to_orbit),
    FAIRING_RECOVERY(R.string.statistics_fairing_recovery, R.drawable.ic_fairing_recovery),
    LAUNCHPADS(R.string.statistics_launchpads, R.drawable.ic_launchpads),
    LANDING_PADS(R.string.statistics_landing_pads, R.drawable.ic_landing_pads)
}