package uk.co.zac_h.spacex.feature.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import uk.co.zac_h.spacex.core.data.LaunchRepository
import uk.co.zac_h.spacex.network.dto.news.ArticleResponse

/**
 * One launch's details, read from the database by [launchId], and the news articles about it.
 * Scoped to its back stack entry, so each opened launch has its own.
 */
@HiltViewModel(assistedFactory = LaunchDetailViewModel.Factory::class)
class LaunchDetailViewModel @AssistedInject constructor(
    @Assisted val launchId: String,
    launchRepository: LaunchRepository,
    newsRepository: LaunchNewsRepository
) : ViewModel() {

    val uiState: StateFlow<LaunchDetailUiState> = launchRepository.launch(launchId)
        .map { launch ->
            launch?.let { LaunchDetailUiState.Loaded(LaunchItem(it)) } ?: LaunchDetailUiState.NotFound
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LaunchDetailUiState.Loading)

    val articles: Flow<PagingData<ArticleResponse>> =
        Pager(PagingConfig(pageSize = 10)) {
            newsRepository.articlesPagingSource(launchId)
        }.flow.cachedIn(viewModelScope)

    @AssistedFactory
    interface Factory {
        fun create(launchId: String): LaunchDetailViewModel
    }
}

sealed interface LaunchDetailUiState {
    data object Loading : LaunchDetailUiState
    data object NotFound : LaunchDetailUiState
    data class Loaded(val launch: LaunchItem) : LaunchDetailUiState
}
