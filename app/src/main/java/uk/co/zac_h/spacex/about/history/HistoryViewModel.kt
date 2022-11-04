package uk.co.zac_h.spacex.about.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.common.types.Order
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.async
import uk.co.zac_h.spacex.network.dto.spacex.History
import uk.co.zac_h.spacex.network.dto.spacex.QueryHistorySort
import uk.co.zac_h.spacex.network.dto.spacex.QueryModel
import uk.co.zac_h.spacex.network.dto.spacex.QueryOptionsModel
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
                sort = QueryHistorySort(order.order),
                limit = 1000000
            )
        )

    var order: Order = repository.getOrder()
        get() = repository.getOrder()
        set(value) {
            field = value
            repository.setOrder(value)
        }

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
}
