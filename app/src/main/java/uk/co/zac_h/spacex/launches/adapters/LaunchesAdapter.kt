package uk.co.zac_h.spacex.launches.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.databinding.ListItemLaunchesBinding
import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.utils.formatDate

class LaunchesAdapter(val onClick: (Launch, View) -> Unit) :
    PagingDataAdapter<Launch, LaunchesAdapter.ViewHolder>(Comparator) {

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
                    patch = launch.links?.missionPatch?.patchSmall
                    vehicle = launch.rocket?.name
                    missionName = launch.missionName
                    date = launch.launchDate?.dateUtc?.formatDate()

                    if (launch.rocket?.name == "Falcon 9") {
                        isReused = launch.cores?.firstOrNull()?.reused ?: false
                        landingPad = launch.cores?.firstOrNull()?.landingPad?.name
                    } else {
                        isReused = false
                        landingPad = null
                    }
                }

                root.setOnClickListener {
                    onClick(launch, root)
                }
            }
        }
    }

    class ViewHolder(val binding: ListItemLaunchesBinding) : RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<Launch>() {

        override fun areItemsTheSame(oldItem: Launch, newItem: Launch) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Launch, newItem: Launch) = oldItem.id == newItem.id
    }
}
