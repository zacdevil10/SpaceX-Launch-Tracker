package uk.co.zac_h.spacex.statistics.graphs.launchmass.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.co.zac_h.spacex.types.LaunchMassViewType
import uk.co.zac_h.spacex.types.RocketType

class LaunchMassFilterViewModel : ViewModel() {

    private val _filterRocket: MutableLiveData<RocketType?> = MutableLiveData(null)
    val filterRocket: LiveData<RocketType?> = _filterRocket

    private val _filterType: MutableLiveData<LaunchMassViewType?> =
        MutableLiveData(LaunchMassViewType.ROCKETS)
    val filterType: LiveData<LaunchMassViewType?> = _filterType

    fun setRocketFilter(value: RocketType?) {
        _filterRocket.value = value
    }

    fun setTypeFilter(value: LaunchMassViewType?) {
        _filterType.value = value
    }

    fun clear() {
        setRocketFilter(null)
        setTypeFilter(LaunchMassViewType.ROCKETS)
    }
}
