package uk.co.zac_h.feature.news.reddit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import uk.co.zac_h.spacex.network.dto.reddit.RedditPost
import javax.inject.Inject

@HiltViewModel
class RedditFeedViewModel @Inject constructor(
    private val repository: RedditFeedRepository
) : ViewModel() {

    val redditFeed: Flow<PagingData<RedditPost>> = Pager(PagingConfig(pageSize = 15)) {
        repository.redditPagingSource
    }.flow.cachedIn(viewModelScope)
}
