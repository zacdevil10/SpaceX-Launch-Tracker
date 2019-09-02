package uk.co.zac_h.spacex.utils

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.github.mikephil.charting.utils.ColorTemplate

fun String.generateCenterSpannableText(): SpannableString =
    SpannableString(this).apply {
        setSpan(RelativeSizeSpan(1.7f), 0, 8, 0)
        setSpan(StyleSpan(Typeface.NORMAL), 8, length - 11, 0)
        setSpan(RelativeSizeSpan(.8f), 8, length - 11, 0)
        setSpan(ForegroundColorSpan(ColorTemplate.rgb("29b6f6")), length - 11, length, 0)
    }