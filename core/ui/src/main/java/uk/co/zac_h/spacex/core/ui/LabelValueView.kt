package uk.co.zac_h.spacex.core.ui

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.use
import androidx.core.view.isVisible

class LabelValueView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val labelTextView = TextView(context)
    private val valueTextView = TextView(context)

    var label: String? = null
        set(value) {
            field = value

            labelTextView.text = value
        }

    var text: String? = null
        set(value) {
            field = value

            isVisible = !value.isNullOrEmpty()
            valueTextView.text = value
        }

    init {
        orientation = HORIZONTAL

        labelTextView.apply {
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            setTypeface(typeface, Typeface.BOLD)
        }

        valueTextView.apply {
            layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT).also {
                it.marginStart = resources.getDimensionPixelSize(R.dimen.medium_margin)
            }
            gravity = Gravity.END
        }

        addView(labelTextView)
        addView(valueTextView)

        context.obtainStyledAttributes(attrs, R.styleable.LabelValueView).use {
            label = it.getString(R.styleable.LabelValueView_label)
            text = it.getString(R.styleable.LabelValueView_android_text)
        }
    }
}