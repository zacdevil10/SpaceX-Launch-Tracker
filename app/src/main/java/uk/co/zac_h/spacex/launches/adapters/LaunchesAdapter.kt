package uk.co.zac_h.spacex.launches.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.NavGraphDirections
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemLaunchesBinding
import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.utils.formatDateMillisLong

class LaunchesAdapter(private val context: Context) :
    ListAdapter<Launch, LaunchesAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemLaunchesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = getItem(position)

        holder.binding.apply {
            root.transitionName = launch.id

            Glide.with(root)
                .load(launch.links?.missionPatch?.patchSmall)
                .error(ContextCompat.getDrawable(context, R.drawable.ic_mission_patch))
                .fallback(ContextCompat.getDrawable(context, R.drawable.ic_mission_patch))
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_mission_patch))
                .into(missionPatch)

            if (launch.rocket?.name == "Falcon 9") {
                reused.visibility =
                    if (launch.cores?.first()?.reused == true) View.VISIBLE else View.GONE

                landingVehicle.visibility =
                    if (launch.cores?.first()?.landingSuccess == true) View.VISIBLE else View.GONE

                landingVehicle.text = launch.cores?.first()?.landingPad?.name
            } else {
                reused.visibility = View.GONE
                landingVehicle.visibility = View.GONE
            }

            flightNumber.text = context.getString(R.string.flight_number, launch.flightNumber)
            vehicle.text = launch.rocket?.name
            missionName.text = launch.missionName
            date.text = launch.launchDate?.dateUnix?.formatDateMillisLong(launch.datePrecision)

            root.setOnClickListener {
                root.findNavController().navigate(
                    NavGraphDirections.actionLaunchItemToLaunchDetailsContainer(
                        launch.missionName,
                        launch.id
                    ),
                    FragmentNavigatorExtras(root to launch.id)
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