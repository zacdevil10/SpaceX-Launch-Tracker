package uk.co.zac_h.spacex.statistics.graphs.launchhistory.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.co.zac_h.spacex.types.LaunchHistoryFilter

class LaunchHistoryFilterViewModel : ViewModel() {

    private val _filter: MutableLiveData<LaunchHistoryFilter?> = MutableLiveData(null)
    val filter: LiveData<LaunchHistoryFilter?> = _filter

    fun setFilter(value: LaunchHistoryFilter?) {
        _filter.value = value
    }

}