package uk.co.zac_h.spacex.base

import androidx.lifecycle.ViewModel
import uk.co.zac_h.spacex.R

class MainViewModel : ViewModel() {

    var isFabVisible: Boolean = false

    val startDestinations = mutableSetOf(
        R.id.dashboard_page_fragment,
        R.id.news_page_fragment,
        R.id.launches_page_fragment,
        R.id.crew_page_fragment,
        R.id.vehicles_page_fragment,
        R.id.statistics_page_fragment
    )
}
