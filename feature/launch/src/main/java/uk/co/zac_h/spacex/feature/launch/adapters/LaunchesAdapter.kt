package uk.co.zac_h.spacex.feature.launch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.core.ui.ExpandedLaunch
import uk.co.zac_h.spacex.core.ui.Launch
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.launch.LaunchItem
import uk.co.zac_h.spacex.feature.launch.databinding.ListItemLaunchesBinding

class LaunchesAdapter(val onClick: (LaunchItem) -> Unit) :
    ListAdapter<LaunchItem, RecyclerView.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> ExpandedViewHolder(
                ListItemLaunchesBinding.inflate(
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
                launchView.setContent {
                    SpaceXTheme {
                        ExpandedLaunch(
                            modifier = Modifier
                                .padding(16.dp),
                            patch = launch.missionPatch,
                            missionName = launch.missionName,
                            date = launch.launchDate,
                            vehicle = launch.rocket,
                            reused = launch.rocket == "Falcon 9" && launch.firstStage?.firstOrNull()?.reused ?: false,
                            landingPad = launch.firstStage?.firstOrNull()?.landingLocation?.let {
                                if (launch.rocket != "Falcon 9" && it == "N/A") null else it
                            },
                            launchSite = launch.launchLocation,
                            description = launch.description,
                            dateUnix = launch.launchDateUnix
                        ) {
                            onClick(launch)
                        }
                    }
                }
            }

            is ViewHolder -> holder.binding.apply {
                launchView.setContent {
                    SpaceXTheme {
                        Launch(
                            modifier = Modifier
                                .clickable { onClick(launch) }
                                .padding(16.dp),
                            patch = launch.missionPatch,
                            missionName = launch.missionName,
                            date = launch.launchDate,
                            vehicle = launch.rocket,
                            reused = launch.rocket == "Falcon 9" && launch.firstStage?.firstOrNull()?.reused ?: false,
                            landingPad = launch.firstStage?.firstOrNull()?.landingLocation?.let {
                                if (launch.rocket != "Falcon 9" && it == "N/A") null else it
                            }
                        )
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int = if (position == 0) {
        0
    } else {
        1
    }

    class ExpandedViewHolder(val binding: ListItemLaunchesBinding) :
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
