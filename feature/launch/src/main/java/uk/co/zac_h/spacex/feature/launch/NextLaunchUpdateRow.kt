package uk.co.zac_h.spacex.feature.launch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.network.TooManyRequestsException
import java.util.concurrent.TimeUnit

/** "Updated 3 min ago" with a refresh button, shown above the next launch card. */
@Composable
fun NextLaunchUpdateRow(
    update: NextLaunchUpdate,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Ticks so the age stays current while the screen is open.
    val now by produceState(System.currentTimeMillis()) {
        while (true) {
            delay(15_000)
            value = System.currentTimeMillis()
        }
    }
    val throttled = update.error as? TooManyRequestsException

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = when {
                throttled != null -> throttled.time?.let { seconds ->
                    val minutes = ((seconds + 59) / 60).coerceAtLeast(1)
                    pluralStringResource(R.plurals.launches_try_again_minutes, minutes, minutes)
                } ?: stringResource(R.string.launches_update_failed)
                update.error != null -> stringResource(R.string.launches_update_failed)
                else -> updatedText(update.fetchedAt, now)
            },
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
            if (update.isRefreshing) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                IconButton(onClick = onRefresh) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = stringResource(R.string.launches_refresh_description)
                    )
                }
            }
        }
    }
}

@Composable
private fun updatedText(fetchedAt: Long?, now: Long): String {
    if (fetchedAt == null) return stringResource(R.string.launches_updated_never)
    val minutes = TimeUnit.MILLISECONDS.toMinutes((now - fetchedAt).coerceAtLeast(0)).toInt()
    val hours = minutes / 60
    val days = hours / 24
    return when {
        minutes < 1 -> stringResource(R.string.launches_updated_just_now)
        hours < 1 -> pluralStringResource(R.plurals.launches_updated_minutes_ago, minutes, minutes)
        days < 1 -> pluralStringResource(R.plurals.launches_updated_hours_ago, hours, hours)
        else -> pluralStringResource(R.plurals.launches_updated_days_ago, days, days)
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun NextLaunchUpdateRowPreview() {
    SpaceXTheme {
        NextLaunchUpdateRow(
            update = NextLaunchUpdate(fetchedAt = System.currentTimeMillis() - 180_000),
            onRefresh = {}
        )
    }
}
