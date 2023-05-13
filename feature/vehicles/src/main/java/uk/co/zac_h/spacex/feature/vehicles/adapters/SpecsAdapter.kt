package uk.co.zac_h.spacex.feature.vehicles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.core.common.utils.TextResource
import uk.co.zac_h.spacex.feature.vehicles.databinding.ListItemVehicleSpecsBinding

class SpecsAdapter : ListAdapter<SpecsItem, SpecsAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ListItemVehicleSpecsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val specs = getItem(position)

        holder.binding.listItemVehicleSpecs.apply {
            label = specs.label.asString(resources)
            text = specs.value.asString(resources)
        }
    }

    class ViewHolder(val binding: ListItemVehicleSpecsBinding) :
        RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<SpecsItem>() {
        override fun areItemsTheSame(oldItem: SpecsItem, newItem: SpecsItem) =
            oldItem.label == newItem.label

        override fun areContentsTheSame(oldItem: SpecsItem, newItem: SpecsItem) =
            oldItem == newItem
    }
}

data class SpecsItem(val label: TextResource, val value: TextResource)