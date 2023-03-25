package uk.co.zac_h.spacex.feature.launch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.feature.launch.CrewItem
import uk.co.zac_h.spacex.feature.launch.R
import uk.co.zac_h.spacex.feature.launch.databinding.ListItemCrewBinding

class LaunchCrewAdapter :
    ListAdapter<CrewItem, LaunchCrewAdapter.ViewHolder>(LaunchCrewComparator) {

    private var expandedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemCrewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val astronaut = getItem(position)

        val isExpanded = position == expandedPosition

        with(holder.binding) {

            Glide.with(root).load(astronaut.image).into(image)

            role.text = astronaut.role
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

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    inner class ViewHolder(val binding: ListItemCrewBinding) : RecyclerView.ViewHolder(binding.root)

    object LaunchCrewComparator : DiffUtil.ItemCallback<CrewItem>() {

        override fun areItemsTheSame(oldItem: CrewItem, newItem: CrewItem) = oldItem == newItem

        override fun areContentsTheSame(oldItem: CrewItem, newItem: CrewItem) =
            oldItem.id == newItem.id
    }
}
