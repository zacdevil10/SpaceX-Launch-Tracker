package uk.co.zac_h.spacex.news.reddit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.network.dto.reddit.RedditPost
import javax.inject.Inject

@HiltViewModel
class RedditFeedViewModel @Inject constructor(
    private val repository: RedditFeedRepository
) : ViewModel() {

    val redditFeed: LiveData<PagingData<RedditPost>> = Pager(PagingConfig(pageSize = 15)) {
        repository.redditPagingSource
    }.liveData.cachedIn(viewModelScope)
}
