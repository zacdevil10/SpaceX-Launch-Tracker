package uk.co.zac_h.spacex.launches.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.databinding.ListItemCrewBinding
import uk.co.zac_h.spacex.launches.CrewItem

class LaunchCrewAdapter(val onClick: (CrewItem) -> Unit) :
    ListAdapter<CrewItem, LaunchCrewAdapter.ViewHolder>(LaunchCrewComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemCrewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val astronaut = getItem(position)

        with(holder.binding) {

            Glide.with(root).load(astronaut.image).into(image)

            role.text = astronaut.role
            title.text = astronaut.name
            agency.text = astronaut.agency

            root.setOnClickListener {
                onClick(astronaut)
            }
        }
    }

    inner class ViewHolder(val binding: ListItemCrewBinding) : RecyclerView.ViewHolder(binding.root)

    object LaunchCrewComparator : DiffUtil.ItemCallback<CrewItem>() {

        override fun areItemsTheSame(oldItem: CrewItem, newItem: CrewItem) = oldItem == newItem

        override fun areContentsTheSame(oldItem: CrewItem, newItem: CrewItem) =
            oldItem.id == newItem.id
    }
}
