package uk.co.zac_h.spacex.utils

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton

interface OnStateChangedAction {

    fun onStateChanged(sheet: View, newState: Int)

}

class ChangeSettingsMenuStateAction(
    private val onShouldShowSettingsMenu: (showSettings: Boolean) -> Unit
) : OnStateChangedAction {

    private var hasCalledShowSettingsMenu: Boolean = false

    override fun onStateChanged(sheet: View, newState: Int) {
        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
            hasCalledShowSettingsMenu = false
            onShouldShowSettingsMenu(false)
        } else {
            if (!hasCalledShowSettingsMenu) {
                hasCalledShowSettingsMenu = true
                onShouldShowSettingsMenu(true)
            }
        }
    }

}

class ShowHideFabStateAction(
    private val fab: FloatingActionButton
) : OnStateChangedAction {

    override fun onStateChanged(sheet: View, newState: Int) {
        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
            fab.show()
        } else {
            fab.hide()
        }
    }

}

class VisibilityStateAction(
    private val view: View
) : OnStateChangedAction {

    override fun onStateChanged(sheet: View, newState: Int) {
        when (newState) {
            BottomSheetBehavior.STATE_HIDDEN -> view.visibility = View.GONE
            else -> view.visibility = View.VISIBLE
        }
    }

}