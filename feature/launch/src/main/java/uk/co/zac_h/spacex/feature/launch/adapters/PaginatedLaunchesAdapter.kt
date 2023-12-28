package uk.co.zac_h.spacex.feature.launch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.core.ui.Launch
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.launch.LaunchItem
import uk.co.zac_h.spacex.feature.launch.databinding.ListItemLaunchesBinding

class PaginatedLaunchesAdapter(val onClick: (LaunchItem) -> Unit) :
    PagingDataAdapter<LaunchItem, PaginatedLaunchesAdapter.ViewHolder>(
        Comparator
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemLaunchesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = getItem(position)

        launch?.let {
            with(holder.binding) {
                launchView.setContent {
                    SpaceXTheme {
                        Launch(
                            modifier = Modifier
                                .clickable { onClick(it) }
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

    class ViewHolder(val binding: ListItemLaunchesBinding) : RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<LaunchItem>() {

        override fun areItemsTheSame(oldItem: LaunchItem, newItem: LaunchItem) = oldItem == newItem

        override fun areContentsTheSame(oldItem: LaunchItem, newItem: LaunchItem) =
            oldItem.id == newItem.id
    }
}
