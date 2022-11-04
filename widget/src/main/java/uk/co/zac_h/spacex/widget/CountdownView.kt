package uk.co.zac_h.spacex.widget

import android.content.Context
import android.util.AttributeSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer
import kotlin.coroutines.CoroutineContext

class CountdownView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs), CoroutineScope {

    private var timer: Timer? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val uiScope = CoroutineScope(coroutineContext)

    var countdown: () -> String? = { null }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelTimer()
    }

    fun startTimer() {
        cancelTimer()
        timer = timer(period = 1000L) {
            uiScope.launch {
                text = countdown()
            }
        }
    }

    private fun cancelTimer() {
        timer?.cancel()
    }
}