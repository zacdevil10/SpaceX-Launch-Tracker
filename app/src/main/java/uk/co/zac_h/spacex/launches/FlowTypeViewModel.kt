package uk.co.zac_h.spacex.launches

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.types.LaunchType
import javax.inject.Inject

@HiltViewModel
class FlowTypeViewModel @Inject constructor() : ViewModel() {

    lateinit var type: LaunchType

}