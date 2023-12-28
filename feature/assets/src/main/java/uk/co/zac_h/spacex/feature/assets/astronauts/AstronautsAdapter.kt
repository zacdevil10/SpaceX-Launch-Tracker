package uk.co.zac_h.spacex.feature.assets.astronauts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.core.ui.Astronaut
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.core.ui.databinding.ListItemAstronautBinding

class AstronautsAdapter :
    PagingDataAdapter<AstronautItem, AstronautsAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemAstronautBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val astronaut = getItem(position)

        holder.binding.listItemCrewCard.setContent {
            SpaceXTheme {
                var expanded by remember { mutableStateOf(false) }

                Astronaut(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    image = astronaut?.image,
                    role = astronaut?.nationality,
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

    object Comparator : DiffUtil.ItemCallback<AstronautItem>() {

        override fun areItemsTheSame(oldItem: AstronautItem, newItem: AstronautItem) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: AstronautItem, newItem: AstronautItem) =
            oldItem.id == newItem.id

    }
}
