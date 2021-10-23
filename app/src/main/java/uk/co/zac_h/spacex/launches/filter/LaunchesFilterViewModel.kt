package uk.co.zac_h.spacex.launches.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchesFilterViewModel @Inject constructor() : ViewModel() {

    var selectedPage: MutableLiveData<Int> = MutableLiveData(0)

    val searchText: MutableLiveData<String> = MutableLiveData("")

}