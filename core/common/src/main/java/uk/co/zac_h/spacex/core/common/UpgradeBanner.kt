package uk.co.zac_h.spacex.core.common

import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import uk.co.zac_h.spacex.core.ui.BannerView
import uk.co.zac_h.spacex.network.TooManyRequestsException
import java.util.concurrent.TimeUnit

fun BannerView.asUpgradeBanner(exception: TooManyRequestsException?, navigation: () -> Unit) {
    primaryActionLabel = resources.getString(R.string.primary_upgrade_label)
    secondaryActionLabel = resources.getString(R.string.secondary_upgrade_label)

    setPrimaryAsDismiss()
    setSecondaryOnClickListener { navigation() }

    isVisible = exception != null
    message = exception?.time?.let { time ->
        val minutes = TimeUnit.SECONDS.toMinutes(time.toLong()).toInt()
        resources.getQuantityString(R.plurals.api_throttled, minutes, minutes)
    } ?: resources.getString(R.string.api_throttled_no_time)
}

fun Fragment.navigateToLearnMore() {
    val request = NavDeepLinkRequest.Builder
        .fromUri("spacex://subscription".toUri())
        .build()

    findNavController().navigate(request)
}