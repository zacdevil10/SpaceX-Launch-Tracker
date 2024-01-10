package uk.co.zac_h.spacex.core.common

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.TooManyRequestsException

@Composable
fun <T> NetworkContent(
    modifier: Modifier = Modifier,
    result: ApiResult<T>,
    retry: () -> Unit,
    content: @Composable (T) -> Unit
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

            is ApiResult.Success -> content(result.result)

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

@Composable
fun <T : Any> NetworkContent(
    modifier: Modifier = Modifier,
    result: LazyPagingItems<T>,
    state: LazyListState,
    content: LazyListScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        when (result.loadState.refresh) {
            is LoadState.Loading -> LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
            )
            is LoadState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Button(
                        modifier = Modifier.align(Alignment.Center),
                        onClick = { result.retry() }
                    ) {
                        Text(text = "Retry")
                    }
                }
            }
            is LoadState.NotLoading -> LazyColumn(
                modifier = modifier,
                state = state
            ) {
                content()

                item {
                    when (result.loadState.append) {
                        is LoadState.Loading -> LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        is LoadState.Error -> Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Button(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(16.dp),
                                onClick = { result.retry() }
                            ) {
                                Text(text = "Retry")
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}