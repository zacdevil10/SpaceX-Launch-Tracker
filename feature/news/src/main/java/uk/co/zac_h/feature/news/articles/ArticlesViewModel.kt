package uk.co.zac_h.feature.news.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val repository: ArticlesRepository
) : ViewModel() {

    val articlesLiveData: Flow<PagingData<ArticleItem>> = Pager(
        PagingConfig(pageSize = 10)
    ) {
        repository.articlesPagingSource
    }.flow.map { data ->
        data.map { ArticleItem(it) }
    }.cachedIn(viewModelScope)
}