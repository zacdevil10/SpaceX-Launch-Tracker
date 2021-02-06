package uk.co.zac_h.spacex.launches.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemFirstStageBinding
import uk.co.zac_h.spacex.model.spacex.LaunchCore
import uk.co.zac_h.spacex.utils.setImageAndTint

class FirstStageAdapter(private val cores: List<LaunchCore>) :
    RecyclerView.Adapter<FirstStageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemFirstStageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val core = cores[position]

        holder.binding.apply {
            firstStageCard.transitionName = core.core?.id

            firstStageCoreSerial.text = core.core?.serial ?: "Unknown"

            firstStageReusedImage.apply {
                core.reused?.let { reused ->
                    if (reused) {
                        setImageAndTint(R.drawable.ic_check_circle_black_24dp, R.color.success)
                    } else {
                        setImageAndTint(R.drawable.ic_remove_circle_black_24dp, R.color.failed)
                    }
                }
            }

            firstStageLandedImage.apply {
                core.landingSuccess?.let { landingSuccess ->
                    if (landingSuccess) {
                        setImageAndTint(R.drawable.ic_check_circle_black_24dp, R.color.success)
                    } else {
                        setImageAndTint(R.drawable.ic_remove_circle_black_24dp, R.color.failed)
                    }
                }
            }

            firstStageLandingImage.apply {
                core.landingAttempt?.let { landingIntent ->
                    if (landingIntent) {
                        when (core.landingType) {
                            "ASDS" -> setImageAndTint(R.drawable.ic_waves, R.color.ocean)
                            "RTLS" -> setImageAndTint(R.drawable.ic_landscape, R.color.landscape)
                        }
                    } else {
                        setImageAndTint(R.drawable.ic_remove_circle_black_24dp, R.color.failed)
                    }
                }
            }

            core.core?.let {
                firstStageDetailsIndicator.visibility = View.VISIBLE
                firstStageCard.setOnClickListener {
                    root.findNavController().navigate(
                        R.id.action_launch_details_container_fragment_to_core_details_fragment,
                        bundleOf("core" to core.core),
                        null,
                        FragmentNavigatorExtras(firstStageCard to (core.core?.id ?: ""))
                    )
                }
            } ?: run {
                firstStageDetailsIndicator.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = cores.size

    class ViewHolder(val binding: ListItemFirstStageBinding) : RecyclerView.ViewHolder(binding.root)
}