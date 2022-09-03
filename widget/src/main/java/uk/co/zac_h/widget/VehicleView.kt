package uk.co.zac_h.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import uk.co.zac_h.widget.databinding.VehicleViewBinding

class VehicleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : MaterialCardView(context, attrs) {

    private val binding: VehicleViewBinding =
        VehicleViewBinding.inflate(LayoutInflater.from(context), this, true)

    var image: String? = null
        set(value) {
            field = value

            Glide.with(this)
                .load(value)
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(binding.vehicleImage)
        }

    var title: String? = null
        set(value) {
            field = value

            binding.vehicleName.text = value
        }

    var block: String? = null
        set(value) {
            field = value

            binding.separator.isVisible = value != null
            binding.block.apply {
                isVisible = value != null
                text = value
            }
        }

    var status: String? = null
        set(value) {
            field = value

            binding.status.text = value
        }

    var buttonText: String? = null
        set(value) {
            field = value

            binding.vehicleSpecs.text = value
        }

    val vehicleSpecs: MaterialButton
        get() = binding.vehicleSpecs

    init {
        cardElevation = resources.getDimension(R.dimen.card_elevation)

        context.obtainStyledAttributes(attrs, R.styleable.VehicleView).use {
            buttonText = it.getString(R.styleable.VehicleView_buttonText)
        }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        child?.let {
            binding.body.addView(child, params)
        } ?: super.addView(child, params)
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        if (params is MarginLayoutParams) {
            params.marginStart = resources.getDimensionPixelSize(R.dimen.xlarge_margin)
            params.marginEnd = resources.getDimensionPixelSize(R.dimen.xlarge_margin)
            params.topMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
            params.bottomMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
        }
        super.setLayoutParams(params)
    }
}