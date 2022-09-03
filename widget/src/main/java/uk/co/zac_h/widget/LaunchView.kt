package uk.co.zac_h.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import uk.co.zac_h.widget.databinding.LaunchViewBinding

class LaunchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding: LaunchViewBinding =
        LaunchViewBinding.inflate(LayoutInflater.from(context), this, true)

    var patch: String? = null
        set(value) {
            field = value

            Glide.with(this)
                .load(value)
                .error(ContextCompat.getDrawable(context, R.drawable.ic_mission_patch))
                .fallback(ContextCompat.getDrawable(context, R.drawable.ic_mission_patch))
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_mission_patch))
                .into(binding.missionPatch)
        }

    var flightNumber: Int? = null
        set(value) {
            field = value

            binding.flightNumber.text = context.getString(R.string.flight_number, value)
        }

    var vehicle: String? = null
        set(value) {
            field = value

            binding.vehicle.text = value
        }

    var missionName: String? = null
        set(value) {
            field = value

            binding.missionName.text = value
        }

    var date: String? = null
        set(value) {
            field = value

            binding.date.text = value
        }

    var isReused: Boolean = false
        set(value) {
            field = value

            binding.reused.isVisible = value
        }

    var landingPad: String? = null
        set(value) {
            field = value

            binding.landingPad.apply {
                isVisible = value != null
                text = value
            }
        }

    init {
        binding.missionName.isSelected = true
    }
}