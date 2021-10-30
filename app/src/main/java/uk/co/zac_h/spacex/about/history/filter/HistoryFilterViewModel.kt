package uk.co.zac_h.spacex.about.history.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryFilterViewModel : ViewModel() {

    val searchText: MutableLiveData<String> = MutableLiveData("")

}