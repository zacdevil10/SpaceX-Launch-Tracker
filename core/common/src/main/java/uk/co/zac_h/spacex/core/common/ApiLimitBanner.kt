package uk.co.zac_h.spacex.core.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.delay
import uk.co.zac_h.spacex.core.ui.Banner
import uk.co.zac_h.spacex.network.TooManyRequestsException

@Composable
fun ApiLimitBanner(
    exception: Throwable
) {
    if (exception is TooManyRequestsException) {
        // Counts down while visible, so the wait shown is always current.
        val secondsRemaining by produceState(exception.secondsRemaining(), exception) {
            while ((value ?: 0) > 0) {
                delay(1_000)
                value = exception.secondsRemaining()
            }
        }

        Banner(
            message = secondsRemaining?.let { seconds ->
                val minutes = ((seconds + 59) / 60).toInt()
                pluralStringResource(R.plurals.api_throttled, minutes, minutes)
            } ?: stringResource(R.string.api_throttled_no_time),
            primaryActionLabel = stringResource(id = R.string.primary_upgrade_label),
            primaryActionAsDismiss = true,
            isVisible = true
        )
    }
}
