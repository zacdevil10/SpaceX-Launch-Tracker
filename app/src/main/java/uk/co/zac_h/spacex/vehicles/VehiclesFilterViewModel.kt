package uk.co.zac_h.spacex.vehicles

import androidx.lifecycle.*
import uk.co.zac_h.spacex.types.Order

class VehiclesFilterViewModel : ViewModel() {

    private val _page = MutableLiveData(0)
    val pageOrder: LiveData<Order?> = _page.switchMap {
        order.map { it[vehiclesPage] }
    }

    var vehiclesPage: VehiclesPage = VehiclesPage.ROCKETS
        private set

    private val _order = MutableLiveData<MutableMap<VehiclesPage, Order>>(mutableMapOf())
    val order: LiveData<MutableMap<VehiclesPage, Order>> = _order

    fun setVehiclesPage(page: Int) {
        vehiclesPage = VehiclesPage.value(page)
        _page.value = page
    }

    fun setOrder(order: Order) {
        _order.value?.put(vehiclesPage, order)
        _order.value = _order.value
    }

    fun reset() {
        _order.value?.put(vehiclesPage, Order.ASCENDING)
        _order.value = _order.value
    }
}

enum class VehiclesPage(val page: Int) {
    ROCKETS(0), DRAGON(1), SHIPS(2), CORES(3), CAPSULES(4);

    companion object {
        fun value(page: Int) = values().first { it.page == page }
    }
}
