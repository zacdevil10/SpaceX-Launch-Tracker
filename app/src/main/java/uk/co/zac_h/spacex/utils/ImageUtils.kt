package uk.co.zac_h.spacex.utils

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R

fun ImageView.setImageAndTint(resId: Int, colorId: Int) {
    setImageResource(resId)
    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, colorId))
}

fun Context.loadPatch(patch: String?, view: ImageView) {
    Glide.with(this)
        .load(patch)
        .error(ContextCompat.getDrawable(this, R.drawable.ic_mission_patch))
        .fallback(ContextCompat.getDrawable(this, R.drawable.ic_mission_patch))
        .placeholder(ContextCompat.getDrawable(this, R.drawable.ic_mission_patch))
        .into(view)
}