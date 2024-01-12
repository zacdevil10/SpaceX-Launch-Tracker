package uk.co.zac_h.spacex.core.common.bottomsheet

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton

interface OnStateChangedAction {

    fun onStateChanged(sheet: View, newState: Int)
}

class ShowHideFabStateAction(
    private val fab: FloatingActionButton,
    private val defaultState: (() -> Boolean)? = null
) : OnStateChangedAction {

    override fun onStateChanged(sheet: View, newState: Int) {
        if (newState != BottomSheetBehavior.STATE_HIDDEN) {
            fab.hide()
        } else {
            defaultState?.invoke()?.let {
                if (it) fab.show()
            } ?: fab.show()
        }
    }
}

class VisibilityStateAction(
    private val view: View
) : OnStateChangedAction {

    override fun onStateChanged(sheet: View, newState: Int) {
        view.visibility = when (newState) {
            BottomSheetBehavior.STATE_HIDDEN, BottomSheetBehavior.STATE_COLLAPSED -> View.GONE
            else -> View.VISIBLE
        }
    }
}

class BackPressedStateAction<T : View>(private val callback: BottomSheetBackPressed<T>) :
    OnStateChangedAction {

    override fun onStateChanged(sheet: View, newState: Int) {
        callback.isEnabled = newState != BottomSheetBehavior.STATE_HIDDEN
    }
}
