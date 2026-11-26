package uk.co.zac_h.spacex.feature.assets

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.common.ListPosition
import javax.inject.Inject

/**
 * The Assets lists' selected tab, scroll positions and expanded astronaut. The lists are composed
 * again when they move between the single-pane and two-pane layouts, for example on rotation, and
 * saved state alone doesn't carry over then; this ViewModel is shared by both.
 */
@HiltViewModel
class AssetsViewModel @Inject constructor() : ViewModel() {

    var selectedPage: Int = 0

    /** The scroll position of each tab's list, by page. */
    val listPositions: MutableMap<Int, ListPosition> = mutableMapOf()

    /**
     * The id of the astronaut picked last: expanded inline in the single-pane layout, and open in
     * the detail pane in the two-pane layout. Null when none is.
     */
    var expandedAstronaut: String? = null

    /** The layout the lists were last shown in, to tell a fold or unfold from other changes. */
    var lastContentType: ContentType? = null
}
