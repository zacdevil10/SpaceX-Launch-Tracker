package uk.co.zac_h.spacex.utils

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import uk.co.zac_h.spacex.R
import kotlin.math.max

class BottomDrawerCallback : BottomSheetBehavior.BottomSheetCallback() {

    private val onSlideActions: MutableList<OnSlideActions> = mutableListOf()
    val onStateChangedActions: MutableList<OnStateChangedAction> = mutableListOf()

    private var lastSlideOffset = -1.0F
    private var halfExpandedSlideOffset = Float.MAX_VALUE

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
        if (halfExpandedSlideOffset == Float.MAX_VALUE) calculateInitialHalfExpandedSlideOffset(
            bottomSheet
        )

        lastSlideOffset = slideOffset

        val trueOffset = if (slideOffset <= halfExpandedSlideOffset) {
            slideOffset.normalize(-1F, halfExpandedSlideOffset, -1F, 0F)
        } else {
            slideOffset.normalize(halfExpandedSlideOffset, 1F, 0F, 1F)
        }

        onSlideActions.forEach { it.onSlide(bottomSheet, trueOffset) }
    }

    override fun onStateChanged(bottomSheet: View, newState: Int) {
        if (newState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
            halfExpandedSlideOffset = lastSlideOffset
            onSlide(bottomSheet, lastSlideOffset)
        }

        onStateChangedActions.forEach { it.onStateChanged(bottomSheet, newState) }
    }

    private fun calculateInitialHalfExpandedSlideOffset(sheet: View) {
        val parent = sheet.parent as CoordinatorLayout
        val behavior = BottomSheetBehavior.from(sheet)

        val halfExpandedOffset = parent.height * (1 - behavior.halfExpandedRatio)
        val peekHeightMin = parent.resources.getDimensionPixelSize(
            R.dimen.design_bottom_sheet_peek_height_min
        )
        val peek = max(peekHeightMin, parent.height - parent.width * 9 / 16)
        val collapsedOffset = max(
            parent.height - peek,
            max(0, parent.height - sheet.height)
        )
        halfExpandedSlideOffset =
            (collapsedOffset - halfExpandedOffset) / (parent.height - collapsedOffset)
    }

    fun addOnSlideAction(action: OnSlideActions): Boolean = onSlideActions.add(action)

    fun addOnStateChangedAction(action: OnStateChangedAction) = onStateChangedActions.add(action)

}

fun Float.normalize(
    inputMin: Float,
    inputMax: Float,
    outputMin: Float,
    outputMax: Float
): Float = when {
    this < inputMin -> outputMin
    this > inputMax -> outputMax
    else -> outputMin * (1 - (this - inputMin) / (inputMax - inputMin)) +
            outputMax * ((this - inputMin) / (inputMax - inputMin))
}