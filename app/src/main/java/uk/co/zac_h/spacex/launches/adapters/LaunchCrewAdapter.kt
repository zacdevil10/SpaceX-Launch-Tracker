package uk.co.zac_h.spacex.launches.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.crew.Crew
import uk.co.zac_h.spacex.databinding.ListItemCrewBinding

class LaunchCrewAdapter : ListAdapter<Crew, LaunchCrewAdapter.ViewHolder>(LaunchCrewComparator) {

    private var expandedPosition = -1

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
            bio.isVisible = isExpanded
            root.isActivated = isExpanded
            expandCollapseToggle.isChecked = isExpanded

            Glide.with(root).load(astronaut.image).into(image)

            role.text = astronaut.role
            title.text = astronaut.name
            agency.text = astronaut.agency
            bio.text = astronaut.bio

            root.setOnClickListener {
                expandedPosition = if (isExpanded) -1 else position
                notifyItemChanged(position)
            }
        }
    }

    inner class ViewHolder(val binding: ListItemCrewBinding) : RecyclerView.ViewHolder(binding.root)

    object LaunchCrewComparator : DiffUtil.ItemCallback<Crew>() {

        override fun areItemsTheSame(oldItem: Crew, newItem: Crew) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Crew, newItem: Crew) = oldItem.id == newItem.id
    }
}
