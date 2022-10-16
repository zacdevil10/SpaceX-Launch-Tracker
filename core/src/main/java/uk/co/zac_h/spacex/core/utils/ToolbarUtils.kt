package uk.co.zac_h.spacex.core.utils

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.Toolbar

fun Toolbar.setupCollapsingToolbar(title: String, icon: Drawable? = null) {
    this.title = title
    navigationIcon = icon
}
