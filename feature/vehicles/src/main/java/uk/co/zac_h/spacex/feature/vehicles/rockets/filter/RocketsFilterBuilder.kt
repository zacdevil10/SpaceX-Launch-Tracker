package uk.co.zac_h.spacex.feature.vehicles.rockets.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import uk.co.zac_h.spacex.core.common.filter.Filter
import uk.co.zac_h.spacex.core.common.filter.FilterOrder
import uk.co.zac_h.spacex.core.common.filter.RocketFamilyFilter
import uk.co.zac_h.spacex.core.common.filter.RocketTypeFilter
import uk.co.zac_h.spacex.core.common.types.Order
import uk.co.zac_h.spacex.core.common.types.RocketFamily
import uk.co.zac_h.spacex.core.common.types.RocketType

class RocketsFilterBuilder : MediatorLiveData<RocketsFilter>() {

    private val _family = MutableLiveData(RocketFamilyFilter())
    val family: LiveData<RocketFamilyFilter> = _family

    private val _type = MutableLiveData(RocketTypeFilter())
    val type: LiveData<RocketTypeFilter> = _type

    private val _order = MutableLiveData(FilterOrder())
    val order: LiveData<FilterOrder> = _order

    init {
        val merge = {
            val familyValue = _family.value
            val typeValue = _type.value
            val orderValue = _order.value

            combine(familyValue, typeValue, orderValue)
        }

        addSource(_family) { value = merge() }
        addSource(_type) { value = merge() }
        addSource(_order) { value = merge() }
    }

    fun filterByFamily(family: RocketFamily) {
        _family.value = RocketFamilyFilter(family)
    }

    fun filterByRocketType(rockets: List<RocketType>?) {
        _type.value = RocketTypeFilter(rockets)
    }

    fun setOrder(order: Order) {
        _order.value = FilterOrder(order)
    }

    fun clear() {
        _family.value = RocketFamilyFilter()
        _type.value = RocketTypeFilter()
        _order.value = FilterOrder()
    }

    private fun combine(vararg filters: Filter?) = RocketsFilter(
        filters.filterIsInstance<RocketFamilyFilter>().firstOrNull() ?: RocketFamilyFilter(),
        filters.filterIsInstance<RocketTypeFilter>().firstOrNull() ?: RocketTypeFilter(),
        filters.filterIsInstance<FilterOrder>().firstOrNull() ?: FilterOrder()
    )
}
