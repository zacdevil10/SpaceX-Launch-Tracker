package uk.co.zac_h.spacex.news.twitter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.network.dto.twitter.TimelineTweetModel
import javax.inject.Inject

@HiltViewModel
class TwitterFeedViewModel @Inject constructor(
    private val repository: TwitterFeedRepository
) : ViewModel() {

    val twitterFeed: LiveData<PagingData<TimelineTweetModel>> = Pager(
        PagingConfig(pageSize = 20)
    ) {
        repository.twitterPagingSource
    }.liveData.cachedIn(viewModelScope)
}
