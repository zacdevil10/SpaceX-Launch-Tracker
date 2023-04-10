package uk.co.zac_h.spacex.feature.launch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.core.common.databinding.ListItemFirstStageBinding
import uk.co.zac_h.spacex.core.common.databinding.ListItemHeaderBinding
import uk.co.zac_h.spacex.core.common.recyclerview.RecyclerViewItem
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.feature.launch.FirstStageItem
import uk.co.zac_h.spacex.feature.launch.R

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

                firstStageSerial.text = core.serial.orUnknown()
                firstStageLandingDescription.text = core.landingDescription
                firstStageLandingLocation.text =
                    if (core.landingAttempt) core.landingLocationFull else null
                firstStageLandingType.text = if (core.landingAttempt) {
                    when (core.landingType) {
                        "ASDS", "RTLS" -> core.landingType
                        else -> null
                    }
                } else null
                firstStagePreviousFlight.text = core.previousFlight?.replace(" | ", "\n")
                firstStageTurnAroundTime.text = core.turnAroundTimeDays?.let {
                    "${core.turnAroundTimeDays} days"
                }
                firstStageTotalFlights.text = core.totalFlights?.toString()
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is Header -> R.layout.list_item_header
        is FirstStageItem -> R.layout.list_item_first_stage
        else -> throw IllegalArgumentException("Invalid item type for FirstStageAdapter")
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
            oldItem == newItem
        } else if (oldItem is Header && newItem is Header) {
            oldItem == newItem
        } else {
            oldItem == newItem
        }
    }
}

data class Header(
    val title: String
) : RecyclerViewItem
