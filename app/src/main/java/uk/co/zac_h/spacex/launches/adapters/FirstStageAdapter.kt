package uk.co.zac_h.spacex.launches.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemFirstStageBinding
import uk.co.zac_h.spacex.model.spacex.LaunchCore
import uk.co.zac_h.spacex.utils.orUnknown
import uk.co.zac_h.spacex.utils.setImageAndTint

class FirstStageAdapter : RecyclerView.Adapter<FirstStageAdapter.ViewHolder>() {

    private var cores: List<LaunchCore> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemFirstStageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val core = cores[position]

        holder.binding.apply {
            firstStageCard.transitionName = core.core?.id

            firstStageCoreSerial.text = core.core?.serial?.orUnknown()

            core.reused?.let { firstStageReusedImage.successFailureImage(it) }

            core.landingSuccess?.let { firstStageLandedImage.successFailureImage(it) }

            firstStageLandingImage.apply {
                core.landingAttempt?.let { landingIntent ->
                    if (landingIntent) when (core.landingType) {
                        "ASDS" -> setImageAndTint(R.drawable.ic_waves, R.color.ocean)
                        "RTLS" -> setImageAndTint(R.drawable.ic_landscape, R.color.landscape)
                    } else successFailureImage(false)
                }
            }

            core.core?.let {
                firstStageDetailsIndicator.visibility = View.VISIBLE
                firstStageCard.setOnClickListener {
                    root.findNavController().navigate(
                        R.id.action_launch_details_container_fragment_to_core_details_fragment,
                        bundleOf("core" to core.core),
                        null,
                        FragmentNavigatorExtras(firstStageCard to (core.core?.id.orEmpty()))
                    )
                }
            } ?: run {
                firstStageDetailsIndicator.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = cores.size

    fun update(list: List<LaunchCore>) {
        cores = list
        notifyDataSetChanged()
    }

    private fun ImageView.successFailureImage(success: Boolean) {
        if (success) {
            setImageAndTint(R.drawable.ic_check_circle_black_24dp, R.color.success)
        } else {
            setImageAndTint(R.drawable.ic_remove_circle_black_24dp, R.color.failed)
        }
    }

    class ViewHolder(val binding: ListItemFirstStageBinding) : RecyclerView.ViewHolder(binding.root)
}