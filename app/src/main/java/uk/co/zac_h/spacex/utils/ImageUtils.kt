package uk.co.zac_h.spacex.utils

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.content.ContextCompat

fun ImageView.setImageAndTint(resId: Int, colorId: Int) {
    setImageResource(resId)
    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, colorId))
}
