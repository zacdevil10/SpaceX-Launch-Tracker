package uk.co.zac_h.spacex.core.bottomsheet

import android.view.View
import androidx.annotation.FloatRange

interface OnSlideActions {

    fun onSlide(
        sheet: View,
        @FloatRange(
            from = -1.0,
            fromInclusive = true,
            to = 1.0,
            toInclusive = true
        ) slideOffset: Float
    )
}

class AlphaSlideAction(
    private val view: View
) : OnSlideActions {

    override fun onSlide(sheet: View, slideOffset: Float) {
        view.alpha = slideOffset.normalize(-1F, 0F, 0F, 1F)
    }
}
