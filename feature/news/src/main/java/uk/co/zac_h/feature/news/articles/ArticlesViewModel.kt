package uk.co.zac_h.feature.news.articles

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.network.dto.news.ArticleResponse
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val repository: ArticlesRepository
) : ViewModel() {

    val articlesLiveData: LiveData<PagingData<ArticleResponse>> = Pager(
        PagingConfig(pageSize = 10)
    ) {
        repository.articlesPagingSource
    }.liveData.cachedIn(viewModelScope)
}