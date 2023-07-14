package uk.co.zac_h.spacex.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import uk.co.zac_h.spacex.core.ui.databinding.BannerViewBinding

class BannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding: BannerViewBinding =
        BannerViewBinding.inflate(LayoutInflater.from(context), this, true)

    var message: String? = null
        set(value) {
            field = value

            binding.message.text = value
        }

    var primaryActionLabel: String? = null
        set(value) {
            field = value

            binding.primaryAction.text = value
            binding.primaryAction.isVisible = value != null
        }

    var secondaryActionLabel: String? = null
        set(value) {
            field = value

            binding.secondaryAction.text = value
            binding.secondaryAction.isVisible = value != null
        }

    fun setPrimaryOnClick(listener: OnClickListener) {
        binding.primaryAction.setOnClickListener(listener)
    }

    fun setSecondaryOnClickListener(listener: OnClickListener) {
        binding.secondaryAction.setOnClickListener(listener)
    }

    init {
        visibility = View.GONE

        context.obtainStyledAttributes(attrs, R.styleable.BannerView).use {
            message = it.getString(R.styleable.BannerView_message)
            primaryActionLabel = it.getString(R.styleable.BannerView_primaryActionLabel)
            secondaryActionLabel = it.getString(R.styleable.BannerView_secondaryActionLabel)
        }
    }

    fun setPrimaryAsDismiss() {
        setPrimaryOnClick { visibility = View.GONE }
    }

    fun setSecondaryAsDismiss() {
        setSecondaryOnClickListener { visibility = View.GONE }
    }
}