package uk.co.zac_h.spacex.utils

import android.view.View
import androidx.viewpager.widget.ViewPager

class DepthPageTransformer : ViewPager.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            when {
                position < -1 -> {
                    alpha = 0f
                }
                position <= 0 -> {
                    alpha = 1f
                    translationX = 0f
                    scaleX = 1f
                    scaleY = 1f
                }
                position <= 1 -> {
                    // Opacity of next page
                    alpha = 1f - position

                    // Offset next page position
                    translationX = ((pageWidth / 1.5) * -position).toFloat()
                }
                else -> {
                    alpha = 0f
                }
            }
        }
    }
}