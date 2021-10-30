package uk.co.zac_h.spacex.utils

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.customview.widget.Openable
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetOpenable<T : View>(private val behavior: BottomSheetBehavior<T>) : Openable {

    override fun isOpen(): Boolean = behavior.state != BottomSheetBehavior.STATE_HIDDEN

    override fun open() {
        behavior.state = if (isOpen) {
            BottomSheetBehavior.STATE_HIDDEN
        } else BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    override fun close() {
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

}

class BottomSheetBackPressed<T : View>(
    private val behavior: BottomSheetBehavior<T>
) : OnBackPressedCallback(false) {
    override fun handleOnBackPressed() {
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }
}