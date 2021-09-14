package uk.co.zac_h.spacex.launches.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemFirstStageBinding
import uk.co.zac_h.spacex.dto.spacex.LaunchCore
import uk.co.zac_h.spacex.launches.details.LaunchDetailsContainerFragmentDirections
import uk.co.zac_h.spacex.utils.orUnknown
import uk.co.zac_h.spacex.utils.setImageAndTint

class FirstStageAdapter :
    ListAdapter<LaunchCore, FirstStageAdapter.ViewHolder>(LaunchCoreComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemFirstStageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val core = getItem(position)

        holder.binding.apply {
            firstStageCard.transitionName = core.core?.id

            firstStageCoreSerial.text = core.core?.serial.orUnknown()

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

            core.core?.let { core ->
                firstStageDetailsIndicator.visibility = View.VISIBLE
                firstStageCard.setOnClickListener {
                    root.findNavController().navigate(
                        LaunchDetailsContainerFragmentDirections.actionLaunchDetailsContainerFragmentToCoreDetailsFragment(
                            core.serial,
                            core.id
                        ),
                        FragmentNavigatorExtras(firstStageCard to (core.id))
                    )
                }
            } ?: run {
                firstStageDetailsIndicator.visibility = View.GONE
            }
        }
    }

    private fun ImageView.successFailureImage(success: Boolean) {
        if (success) {
            setImageAndTint(R.drawable.ic_check_circle_black_24dp, R.color.success)
        } else {
            setImageAndTint(R.drawable.ic_remove_circle_black_24dp, R.color.failed)
        }
    }

    class ViewHolder(val binding: ListItemFirstStageBinding) : RecyclerView.ViewHolder(binding.root)

    object LaunchCoreComparator : DiffUtil.ItemCallback<LaunchCore>() {

        override fun areItemsTheSame(oldItem: LaunchCore, newItem: LaunchCore) = oldItem == newItem

        override fun areContentsTheSame(oldItem: LaunchCore, newItem: LaunchCore) =
            oldItem.id == newItem.id
                    && oldItem.core?.id == newItem.core?.id
                    && oldItem.reused == newItem.reused
                    && oldItem.landingSuccess == newItem.landingSuccess
                    && oldItem.landingAttempt == newItem.landingAttempt
                    && oldItem.core?.serial == newItem.core?.serial
    }
}