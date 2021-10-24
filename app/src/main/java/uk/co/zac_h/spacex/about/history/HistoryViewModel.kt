package uk.co.zac_h.spacex.about.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.async
import uk.co.zac_h.spacex.dto.spacex.History
import uk.co.zac_h.spacex.dto.spacex.QueryHistorySort
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.utils.Keys.HistoryKeys
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel
import uk.co.zac_h.spacex.utils.splitHistoryListByDate
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _history = MutableLiveData<ApiResult<List<HistoryHeaderModel>>>()
    val history: LiveData<ApiResult<List<HistoryHeaderModel>>> = _history

    private val query: QueryModel
        get() = QueryModel(
            options = QueryOptionsModel(
                pagination = false,
                sort = QueryHistorySort(if (repository.getOrder()) HistoryKeys.ORDER_DESCENDING else HistoryKeys.ORDER_ASCENDING),
                limit = 1000000
            )
        )

    fun getHistory(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_history) {
                repository.fetch("History", query, cachePolicy)
            }

            _history.value = response.await().map {
                splitHistoryListByDate(it.docs.map { item -> History(item) })
            }
        }
    }

    fun getOrder() = repository.getOrder()

    fun setOrder(order: Boolean) = repository.setOrder(order)

}