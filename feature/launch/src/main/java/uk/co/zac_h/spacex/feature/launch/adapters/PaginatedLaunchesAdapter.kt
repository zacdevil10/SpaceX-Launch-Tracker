package uk.co.zac_h.spacex.feature.launch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
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
                root.transitionName = launch.id

                launchView.apply {
                    patch = launch.missionPatch
                    vehicle = launch.rocket
                    missionName = launch.missionName
                    date = launch.launchDate

                    if (launch.rocket == "Falcon 9") {
                        isReused = launch.firstStage?.firstOrNull()?.reused ?: false
                        landingPad = launch.firstStage?.firstOrNull()?.landingLocation
                    } else {
                        isReused = false
                        landingPad = null
                    }
                }

                root.setOnClickListener {
                    onClick(launch)
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
