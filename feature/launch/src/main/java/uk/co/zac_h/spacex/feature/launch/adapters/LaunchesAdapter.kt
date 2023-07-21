package uk.co.zac_h.spacex.feature.launch.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.core.ui.LaunchView
import uk.co.zac_h.spacex.feature.launch.LaunchItem
import uk.co.zac_h.spacex.feature.launch.R
import uk.co.zac_h.spacex.feature.launch.databinding.ListItemLaunchesBinding
import uk.co.zac_h.spacex.feature.launch.databinding.ListItemLaunchesExpandedBinding

class LaunchesAdapter(val onClick: (LaunchItem, View) -> Unit) :
    ListAdapter<LaunchItem, RecyclerView.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.list_item_launches_expanded -> ExpandedViewHolder(
                ListItemLaunchesExpandedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> ViewHolder(
                ListItemLaunchesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val launch = getItem(position)

        when (holder) {
            is ExpandedViewHolder -> holder.binding.apply {
                val remaining = launch.countdown(root.resources)

                countdown.isVisible = remaining != null
                countdown.countdown = { launch.countdown(root.resources) }
                if (remaining != null) countdown.startTimer()

                launchView.bind(launch)

                siteNameText.text = launch.launchLocation
                descriptionText.text = launch.description

                root.setOnClickListener {
                    onClick(launch, root)
                }
            }

            is ViewHolder -> holder.binding.apply {
                root.transitionName = launch.id

                launchView.bind(launch)

                root.setOnClickListener {
                    onClick(launch, root)
                }
            }
        }
    }

    private fun LaunchView.bind(launch: LaunchItem) {
        patch = launch.missionPatch
        vehicle = launch.rocket
        missionName = launch.missionName
        date = launch.launchDate

        if (launch.rocket == "Falcon 9") {
            isReused = launch.firstStage?.firstOrNull()?.reused ?: false
            landingPad = launch.firstStage?.firstOrNull()?.landingLocation?.let {
                if (it == "N/A") null else it
            }
        } else {
            isReused = false
            landingPad = null
        }
    }

    override fun getItemViewType(position: Int): Int = if (position == 0) {
        R.layout.list_item_launches_expanded
    } else {
        R.layout.list_item_launches
    }

    class ExpandedViewHolder(val binding: ListItemLaunchesExpandedBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ViewHolder(val binding: ListItemLaunchesBinding) : RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<LaunchItem>() {

        override fun areItemsTheSame(
            oldItem: LaunchItem,
            newItem: LaunchItem
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: LaunchItem,
            newItem: LaunchItem
        ) = oldItem == newItem
    }
}
