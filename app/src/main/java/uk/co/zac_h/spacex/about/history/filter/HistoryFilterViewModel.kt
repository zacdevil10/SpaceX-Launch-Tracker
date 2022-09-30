package uk.co.zac_h.spacex.about.history.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.about.history.HistoryRepository
import uk.co.zac_h.spacex.types.Order
import javax.inject.Inject

@HiltViewModel
class HistoryFilterViewModel @Inject constructor(
    repository: HistoryRepository
) : ViewModel() {

    private val _order = MutableLiveData(repository.getOrder())
    val order: LiveData<Order> = _order

    fun setOrder(order: Order) {
        _order.value = order
    }

    fun reset() {
        _order.value = Order.DESCENDING
    }
}
