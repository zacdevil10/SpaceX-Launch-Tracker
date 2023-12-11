package uk.co.zac_h.spacex.core.common

import androidx.core.view.isVisible
import uk.co.zac_h.spacex.core.ui.BannerView
import uk.co.zac_h.spacex.network.TooManyRequestsException
import java.util.concurrent.TimeUnit

fun BannerView.apiLimitReached(exception: TooManyRequestsException?) {
    primaryActionLabel = resources.getString(R.string.primary_upgrade_label)

    setPrimaryAsDismiss()

    isVisible = exception != null
    message = exception?.time?.let { time ->
        val minutes = TimeUnit.SECONDS.toMinutes(time.toLong()).toInt()
        resources.getQuantityString(R.plurals.api_throttled, minutes, minutes)
    } ?: resources.getString(R.string.api_throttled_no_time)
}