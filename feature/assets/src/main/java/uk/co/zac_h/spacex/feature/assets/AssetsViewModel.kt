package uk.co.zac_h.spacex.feature.assets

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.core.common.ListPosition
import javax.inject.Inject

/**
 * The Assets lists' selected tab, and scroll positions. The lists are composed
 * again when they move between the single-pane and two-pane layouts, for example on rotation, and
 * saved state alone doesn't carry over then; this ViewModel is shared by both.
 */
@HiltViewModel
class AssetsViewModel @Inject constructor() : ViewModel() {

    var selectedPage: Int = 0

    /** The scroll position of each tab's list, by page. */
    val listPositions: MutableMap<Int, ListPosition> = mutableMapOf()
}
