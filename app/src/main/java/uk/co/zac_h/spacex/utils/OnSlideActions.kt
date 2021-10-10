package uk.co.zac_h.spacex.utils

import android.view.View
import androidx.annotation.FloatRange
import com.google.android.material.shape.MaterialShapeDrawable

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

class ContainerSheetTransformSlideAction(
    private val container: View,
    private val foregroundShapeDrawable: MaterialShapeDrawable
) : OnSlideActions {

    override fun onSlide(sheet: View, slideOffset: Float) {
        val progress = slideOffset.normalize(0F, 0.25F, 1F, 0F)
        foregroundShapeDrawable.interpolation = progress
    }
}

class AlphaSlideAction(
    private val view: View
) : OnSlideActions {

    override fun onSlide(sheet: View, slideOffset: Float) {
        view.alpha = slideOffset.normalize(-1F, 0F, 0F, 1F)
    }

}