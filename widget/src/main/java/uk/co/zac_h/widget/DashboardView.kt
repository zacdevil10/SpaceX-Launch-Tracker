package uk.co.zac_h.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.use
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.LinearProgressIndicator
import uk.co.zac_h.widget.databinding.DashboardViewBinding

class DashboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : MaterialCardView(context, attrs) {

    private val binding: DashboardViewBinding =
        DashboardViewBinding.inflate(LayoutInflater.from(context), this, true)

    val launchView: LaunchView
        get() = binding.launchView

    val progress: LinearProgressIndicator
        get() = binding.progress

    var countdown: String? = null
        set(value) {
            field = value

            binding.countdown.isVisible = value != null
            binding.heading.isVisible = value == null
            value?.let { update(it) }
        }

    lateinit var type: DashboardViewType

    init {
        cardElevation = resources.getDimension(R.dimen.card_elevation)

        context.obtainStyledAttributes(attrs, R.styleable.DashboardView).use {
            it.getInt(R.styleable.DashboardView_type, 0).apply {
                type = DashboardViewType.fromValue(this)
            }
        }

        when (type) {
            DashboardViewType.NEXT -> {
                binding.heading.setText(R.string.next_launch_heading)
            }
            DashboardViewType.LATEST -> {
                binding.heading.setText(R.string.latest_launch_heading)
            }
            DashboardViewType.PINNED -> {
                binding.heading.setText(R.string.pinned_launches_heading)
            }
        }
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        if (params is MarginLayoutParams) {
            params.marginStart = resources.getDimensionPixelSize(R.dimen.medium_margin)
            params.marginEnd = resources.getDimensionPixelSize(R.dimen.medium_margin)
            params.topMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
            params.bottomMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
        }
        super.setLayoutParams(params)
    }

    fun update(time: String) {
        binding.countdown.text = time
    }

    fun finish(onClickListener: OnClickListener) {
        if (type != DashboardViewType.NEXT) {
            throw IllegalStateException("Specified type does not have a countdown")
        }
        binding.countdown.isVisible = false
        binding.watchNow.apply {
            isVisible = true
            setOnClickListener(onClickListener)
        }
    }
    
    enum class DashboardViewType(val value: Int) {
        NEXT(0),
        LATEST(1),
        PINNED(2);

        companion object {
            fun fromValue(value: Int?) = values().first { it.value == value }
        }
    }
}