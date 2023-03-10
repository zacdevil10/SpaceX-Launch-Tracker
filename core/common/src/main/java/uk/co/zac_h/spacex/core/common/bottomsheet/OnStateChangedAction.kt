package uk.co.zac_h.spacex.core.common.bottomsheet

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.customview.widget.Openable
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

class StandardBackPressedStateAction<T : View>(private val callback: StandardBottomSheetBackPressed<T>) :
    OnStateChangedAction {

    override fun onStateChanged(sheet: View, newState: Int) {
        callback.isEnabled = newState != BottomSheetBehavior.STATE_COLLAPSED
    }
}

class HideBottomSheet(
    private val openable: Openable
) : OnStateChangedAction {

    override fun onStateChanged(sheet: View, newState: Int) {
        openable.close()
    }
}

class HideKeyboard(
    private val context: Context,
    private val view: IBinder
) : OnStateChangedAction {

    override fun onStateChanged(sheet: View, newState: Int) {
        val ime = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
            ime.hideSoftInputFromWindow(view, 0)
        }
    }
}
