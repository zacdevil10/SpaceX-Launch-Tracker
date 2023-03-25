package uk.co.zac_h.spacex.feature.vehicles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.core.common.utils.metricFormat
import uk.co.zac_h.spacex.feature.vehicles.R
import uk.co.zac_h.spacex.feature.vehicles.databinding.ListItemRocketPayloadBinding
import uk.co.zac_h.spacex.feature.vehicles.rockets.PayloadWeights

class RocketPayloadAdapter :
    ListAdapter<PayloadWeights, RocketPayloadAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemRocketPayloadBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val payload = getItem(position)

        holder.binding.apply {
            listItemRocketPayloadMass.apply {
                label = payload.name
                text = resources.getString(
                    R.string.mass,
                    payload.mass.metricFormat(),
                )
            }
        }
    }

    class ViewHolder(val binding: ListItemRocketPayloadBinding) :
        RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<PayloadWeights>() {

        override fun areItemsTheSame(oldItem: PayloadWeights, newItem: PayloadWeights) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: PayloadWeights, newItem: PayloadWeights) =
            oldItem.name == newItem.name
    }
}
