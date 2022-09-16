package uk.co.zac_h.spacex.launches.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.NavGraphDirections
import uk.co.zac_h.spacex.databinding.ListItemLaunchesBinding
import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.utils.formatDateMillisLong

class LaunchesAdapter : ListAdapter<Launch, LaunchesAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemLaunchesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = getItem(position)

        with(holder.binding) {
            root.transitionName = launch.id

            launchView.apply {
                patch = launch.links?.missionPatch?.patchSmall
                flightNumber = launch.flightNumber
                vehicle = launch.rocket?.name
                missionName = launch.missionName
                date = launch.launchDate?.dateUnix?.formatDateMillisLong(launch.datePrecision)

                if (launch.rocket?.name == "Falcon 9") {
                    isReused = launch.cores?.first()?.reused ?: false
                    landingPad = launch.cores?.first()?.landingPad?.name
                } else {
                    isReused = false
                    landingPad = null
                }
            }

            root.setOnClickListener {
                root.findNavController().navigate(
                    NavGraphDirections.actionLaunchItemToLaunchDetailsContainer(
                        launch.missionName,
                        launch.id.orEmpty()
                    ),
                    FragmentNavigatorExtras(root to launch.id.orEmpty())
                )
            }
        }
    }

    class ViewHolder(val binding: ListItemLaunchesBinding) : RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<Launch>() {

        override fun areItemsTheSame(oldItem: Launch, newItem: Launch) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Launch, newItem: Launch) = oldItem.id == newItem.id
    }
}