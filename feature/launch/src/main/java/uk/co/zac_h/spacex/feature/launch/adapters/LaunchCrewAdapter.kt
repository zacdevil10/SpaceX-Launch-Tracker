package uk.co.zac_h.spacex.feature.launch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.core.ui.AstronautView
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.core.ui.databinding.ListItemAstronautBinding
import uk.co.zac_h.spacex.feature.launch.CrewItem

class LaunchCrewAdapter :
    ListAdapter<CrewItem, LaunchCrewAdapter.ViewHolder>(LaunchCrewComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemAstronautBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val astronaut = getItem(position)

        holder.binding.listItemCrewCard.setContent {
            SpaceXTheme {
                var expanded by remember { mutableStateOf(false) }

                AstronautView(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    image = astronaut?.image,
                    role = astronaut?.role,
                    title = astronaut?.name,
                    agency = astronaut?.agency,
                    status = astronaut?.status?.status,
                    firstFlight = astronaut?.firstFlight,
                    description = astronaut?.bio,
                    expanded = expanded
                ) {
                    expanded = !expanded
                }
            }
        }
    }

    class ViewHolder(val binding: ListItemAstronautBinding) : RecyclerView.ViewHolder(binding.root)

    object LaunchCrewComparator : DiffUtil.ItemCallback<CrewItem>() {

        override fun areItemsTheSame(oldItem: CrewItem, newItem: CrewItem) = oldItem == newItem

        override fun areContentsTheSame(oldItem: CrewItem, newItem: CrewItem) =
            oldItem.id == newItem.id
    }
}
