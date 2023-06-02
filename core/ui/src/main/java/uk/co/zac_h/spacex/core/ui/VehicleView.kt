package uk.co.zac_h.spacex.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.use
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import uk.co.zac_h.spacex.core.ui.databinding.VehicleViewBinding

class VehicleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : MaterialCardView(context, attrs) {

    private val binding: VehicleViewBinding =
        VehicleViewBinding.inflate(LayoutInflater.from(context), this, true)

    var image: String? = null
        set(value) {
            field = value

            binding.vehicleImage.isVisible = value != null

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
        context.obtainStyledAttributes(attrs, R.styleable.VehicleView).use {
            buttonText = it.getString(R.styleable.VehicleView_buttonText)
        }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        child?.let {
            binding.body.addView(child, params)
        } ?: super.addView(null, params)
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        if (params is MarginLayoutParams) {
            params.marginStart = resources.getDimensionPixelSize(R.dimen.medium_margin)
            params.marginEnd = resources.getDimensionPixelSize(R.dimen.medium_margin)
            params.topMargin = resources.getDimensionPixelSize(R.dimen.medium_margin)
            params.bottomMargin = resources.getDimensionPixelSize(R.dimen.medium_margin)
        }
        super.setLayoutParams(params)
    }
}
