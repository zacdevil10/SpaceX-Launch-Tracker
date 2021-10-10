package uk.co.zac_h.spacex.crew.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.crew.CrewFragmentDirections
import uk.co.zac_h.spacex.databinding.GridItemCrewBinding
import uk.co.zac_h.spacex.dto.spacex.Crew

class CrewAdapter : ListAdapter<Crew, CrewAdapter.ViewHolder>(CrewComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        GridItemCrewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = getItem(position)

        holder.binding.apply {
            root.transitionName = person.id

            Glide.with(root).load(person.image).into(image)

            title.text = person.name

            gridItemCrewConstraint.setOnClickListener {
                holder.bind(person)
            }
        }
    }

    inner class ViewHolder(val binding: GridItemCrewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(person: Crew) {
            binding.root.findNavController().navigate(
                CrewFragmentDirections.actionCrewFragmentToCrewDetailsFragment(person.id),
                FragmentNavigatorExtras(binding.root to person.id)
            )
        }
    }

    object CrewComparator : DiffUtil.ItemCallback<Crew>() {

        override fun areItemsTheSame(oldItem: Crew, newItem: Crew) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Crew, newItem: Crew) = oldItem.id == newItem.id

    }
}