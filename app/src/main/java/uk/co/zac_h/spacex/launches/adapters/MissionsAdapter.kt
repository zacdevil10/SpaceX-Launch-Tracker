package uk.co.zac_h.spacex.launches.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemMissionBinding
import uk.co.zac_h.spacex.model.spacex.Launch

class MissionsAdapter(
    private val context: Context?,
    private val launches: List<Launch>
) :
    RecyclerView.Adapter<MissionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemMissionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = launches[position]

        holder.binding.apply {
            root.transitionName = launch.id
            listItemMissionNameText.text = launch.missionName
            listItemMissionFlightText.text =
                context?.getString(R.string.flight_number, launch.flightNumber)

            root.setOnClickListener {
                root.findNavController().navigate(
                    R.id.missions_to_launch_details_container_fragment,
                    bundleOf("launch_id" to launch.id),
                    null,
                    FragmentNavigatorExtras(root to launch.id)
                )
            }
        }
    }

    override fun getItemCount(): Int = launches.size

    class ViewHolder(val binding: ListItemMissionBinding) : RecyclerView.ViewHolder(binding.root)
}