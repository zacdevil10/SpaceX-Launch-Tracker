package uk.co.zac_h.spacex.utils.views

import android.annotation.SuppressLint
import android.content.Context
import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.method.Touch
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat

class HtmlTextView : AppCompatTextView {

    private var linkHit = false
    private var consumeNonUrlClicks = false
    var plainText = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        linkHit = false

        val res = super.onTouchEvent(event)

        if (!consumeNonUrlClicks) return linkHit

        return res
    }

    fun setHtmlText(text: String) {
        val htmlText = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)

        setText(if (!plainText) htmlText else htmlText.toString().replace("\n", " "))
    }

    override fun hasFocusable(): Boolean = false

    object LocalLinkMovementMethod : LinkMovementMethod() {

        override fun onTouchEvent(
            widget: TextView,
            buffer: Spannable,
            event: MotionEvent?
        ): Boolean {
            val action = event?.action

            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                var x = event.x
                var y = event.y.toInt()

                x -= widget.totalPaddingLeft
                y -= widget.totalPaddingTop

                x += widget.scrollX
                y += widget.scrollY

                val layout = widget.layout
                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x)

                val links = buffer.getSpans(off, off, ClickableSpan::class.java)

                if (links.isNotEmpty()) {
                    if (action == MotionEvent.ACTION_UP) {
                        links[0].onClick(widget)
                    } else {
                        Selection.setSelection(
                            buffer,
                            buffer.getSpanStart(links[0]),
                            buffer.getSpanEnd(links[0])
                        )
                    }

                    if (widget is HtmlTextView) {
                        widget.linkHit = true
                    }

                    return true
                } else {
                    Selection.removeSelection(buffer)
                    Touch.onTouchEvent(widget, buffer, event)
                    return false
                }
            }

            return Touch.onTouchEvent(widget, buffer, event)
        }
    }
}