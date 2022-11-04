package uk.co.zac_h.spacex.feature.launch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.core.common.image.setImageAndTint
import uk.co.zac_h.spacex.core.common.recyclerview.RecyclerViewItem
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.feature.launch.FirstStageItem
import uk.co.zac_h.spacex.feature.launch.R
import uk.co.zac_h.spacex.feature.launch.databinding.ListItemFirstStageBinding
import uk.co.zac_h.spacex.feature.launch.databinding.ListItemHeaderBinding

class FirstStageAdapter :
    ListAdapter<RecyclerViewItem, RecyclerView.ViewHolder>(LaunchCoreComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.list_item_header -> HeaderViewHolder(
                ListItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.list_item_first_stage -> ViewHolder(
                ListItemFirstStageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid viewType for FirstStageAdapter")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val core = getItem(position)

        when (holder) {
            is HeaderViewHolder -> holder.binding.apply {
                core as Header
                header.text = core.title
            }
            is ViewHolder -> holder.binding.apply {
                core as FirstStageItem

                firstStageCard.transitionName = core.id

                firstStageCoreSerial.text = core.serial.orUnknown()

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
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is Header -> R.layout.list_item_header
        is FirstStageItem -> R.layout.list_item_first_stage
        else -> throw IllegalArgumentException("Invalid item type for FirstStageAdapter")
    }

    private fun ImageView.successFailureImage(success: Boolean) {
        if (success) {
            setImageAndTint(R.drawable.ic_check_circle_black_24dp, R.color.success)
        } else {
            setImageAndTint(R.drawable.ic_remove_circle_black_24dp, R.color.failed)
        }
    }

    class HeaderViewHolder(val binding: ListItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ViewHolder(val binding: ListItemFirstStageBinding) : RecyclerView.ViewHolder(binding.root)

    object LaunchCoreComparator : DiffUtil.ItemCallback<RecyclerViewItem>() {

        override fun areItemsTheSame(
            oldItem: RecyclerViewItem,
            newItem: RecyclerViewItem
        ) = if (oldItem is FirstStageItem && newItem is FirstStageItem) {
            oldItem.id == newItem.id
        } else {
            oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RecyclerViewItem,
            newItem: RecyclerViewItem
        ) = if (oldItem is FirstStageItem && newItem is FirstStageItem) {
            oldItem.serial == newItem.serial
                    && oldItem.type == newItem.type
                    && oldItem.reused == newItem.reused
                    && oldItem.landingAttempt == newItem.landingAttempt
                    && oldItem.landingSuccess == newItem.landingSuccess
                    && oldItem.landingType == newItem.landingType
        } else if (oldItem is Header && newItem is Header) {
            oldItem.title == newItem.title
        } else {
            oldItem == newItem
        }
    }
}

data class Header(
    val title: String
) : RecyclerViewItem
