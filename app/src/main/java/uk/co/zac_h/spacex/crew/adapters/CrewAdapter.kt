package uk.co.zac_h.spacex.crew.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.crew.AstronautItem
import uk.co.zac_h.spacex.feature.launch.R
import uk.co.zac_h.spacex.feature.launch.databinding.ListItemCrewBinding

class CrewAdapter : PagingDataAdapter<AstronautItem, CrewAdapter.ViewHolder>(CrewComparator) {

    private var expandedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemCrewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val astronaut = getItem(position)

        val isExpanded = position == expandedPosition

        with(holder.binding) {
            astronaut?.let {
                Glide.with(root).load(astronaut.image).into(image)

                role.text = astronaut.nationality
                title.text = astronaut.name
                agency.text = astronaut.agency
                content.isVisible = isExpanded

                listItemCrewCard.strokeWidth = if (isExpanded) {
                    root.resources.getDimensionPixelSize(R.dimen.launch_crew_stoke_width)
                } else 0

                status.text = astronaut.status.status
                firstFlight.text = astronaut.firstFlight
                bio.text = astronaut.bio

                headerCard.setOnClickListener {
                    expandedPosition = if (isExpanded) -1 else position
                    TransitionManager.beginDelayedTransition(listItemCrewCard)
                    notifyItemChanged(position, astronaut)
                }
            }
        }
    }

    class ViewHolder(val binding: ListItemCrewBinding) : RecyclerView.ViewHolder(binding.root)

    object CrewComparator : DiffUtil.ItemCallback<AstronautItem>() {

        override fun areItemsTheSame(oldItem: AstronautItem, newItem: AstronautItem) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: AstronautItem, newItem: AstronautItem) =
            oldItem.id == newItem.id

    }
}
