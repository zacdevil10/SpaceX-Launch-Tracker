package uk.co.zac_h.spacex.launches.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.NavGraphDirections
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemMissionBinding
import uk.co.zac_h.spacex.dto.spacex.Launch

class MissionsAdapter(
    private val context: Context,
    private val launches: List<Launch>
) : RecyclerView.Adapter<MissionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemMissionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = launches[position]

        holder.binding.apply {
            root.transitionName = launch.id
            missionName.text = launch.missionName
            missionFlightNumber.text =
                context.getString(R.string.flight_number, launch.flightNumber)

            root.setOnClickListener {
                root.findNavController().navigate(
                    NavGraphDirections.missionsToLaunchDetailsContainerFragment(
                        launch.missionName,
                        launch.id
                    ),
                    FragmentNavigatorExtras(root to launch.id)
                )
            }
        }
    }

    override fun getItemCount(): Int = launches.size

    class ViewHolder(val binding: ListItemMissionBinding) : RecyclerView.ViewHolder(binding.root)
}