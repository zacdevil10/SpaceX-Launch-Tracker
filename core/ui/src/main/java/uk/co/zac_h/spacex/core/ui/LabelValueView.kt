package uk.co.zac_h.spacex.core.ui

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@Composable
fun LabelValueView(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold)
        Text(text = value, modifier = Modifier.padding(start = 16.dp))
    }
}

@Composable
@Preview
fun LabelValueViewPreview() {
    LabelValueView(
        modifier = Modifier.background(Color.White),
        label = "Label",
        value = "Value"
    )
}