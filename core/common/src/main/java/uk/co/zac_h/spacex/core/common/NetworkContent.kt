package uk.co.zac_h.spacex.core.common

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.TooManyRequestsException

@Composable
fun <T> NetworkContent(
    modifier: Modifier = Modifier,
    result: ApiResult<T>,
    retry: () -> Unit,
    content: @Composable (ApiResult.Success<T>) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        ApiLimitBanner(exception = (result as? ApiResult.Failure)?.exception as? TooManyRequestsException)

        when (result) {
            is ApiResult.Pending -> LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
            )

            is ApiResult.Success -> content(result)

            is ApiResult.Failure -> {
                if (result.exception !is TooManyRequestsException) {
                    Toast.makeText(context, result.exception.message, Toast.LENGTH_SHORT).show()
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Button(
                        modifier = Modifier.align(Alignment.Center),
                        onClick = { retry() }
                    ) {
                        Text(text = "Retry")
                    }
                }
            }
        }
    }
}