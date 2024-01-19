package uk.co.zac_h.spacex.core.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import uk.co.zac_h.spacex.core.ui.Banner
import uk.co.zac_h.spacex.network.TooManyRequestsException
import java.util.concurrent.TimeUnit

@Composable
fun ApiLimitBanner(
    exception: TooManyRequestsException?
) {
    AnimatedVisibility(visible = exception != null) {
        Banner(
            message = exception?.time?.let { time ->
                val minutes = TimeUnit.SECONDS.toMinutes(time.toLong()).toInt()
                pluralStringResource(R.plurals.api_throttled, minutes, minutes)
            } ?: stringResource(R.string.api_throttled_no_time),
            primaryActionLabel = stringResource(id = R.string.primary_upgrade_label),
            primaryActionAsDismiss = true
        )
    }
}