package uk.co.zac_h.spacex.core.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    Column(
        modifier = modifier
    ) {
        ApiLimitBanner(exception = (result as? ApiResult.Failure)?.exception as? TooManyRequestsException)

        when (result) {
            is ApiResult.Pending -> LoadingIndicator()
            is ApiResult.Success -> content(result.result)
            is ApiResult.Failure -> NetworkError(retry = retry)
        }
    }
}

@Composable
fun <T : Any> NetworkContent(
    modifier: Modifier = Modifier,
    result: LazyPagingItems<T>,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        when (result.loadState.refresh) {
            is LoadState.Loading -> LoadingIndicator()
            is LoadState.Error -> NetworkError(retry = result::retry)
            is LoadState.NotLoading -> content()
        }
    }
}

@Composable
fun <T : Any> NetworkVerticalListContent(
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
            is LoadState.Loading -> LoadingIndicator()
            is LoadState.Error -> NetworkError(retry = result::retry)
            is LoadState.NotLoading -> LazyColumn(
                modifier = modifier,
                state = state
            ) {
                content()

                pagingFooter(loadState = result.loadState.append, retry = result::retry)
            }
        }
    }
}

fun LazyListScope.pagingFooter(loadState: LoadState, retry: () -> Unit) {
    item {
        when (loadState) {
            is LoadState.Loading -> LoadingIndicator()
            is LoadState.Error -> PagingError(retry = retry)
            else -> {}
        }
    }
}

fun LazyStaggeredGridScope.pagingFooter(loadState: LoadState, retry: () -> Unit) {
    item {
        when (loadState) {
            is LoadState.Loading -> LoadingIndicator()
            is LoadState.Error -> PagingError(retry = retry)
            else -> {}
        }
    }
}

@Composable
internal fun LoadingIndicator() {
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
internal fun NetworkError(retry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = retry
        ) {
            Text(text = "Retry")
        }
    }
}

@Composable
internal fun PagingError(
    retry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            onClick = retry
        ) {
            Text(text = "Retry")
        }
    }
}
