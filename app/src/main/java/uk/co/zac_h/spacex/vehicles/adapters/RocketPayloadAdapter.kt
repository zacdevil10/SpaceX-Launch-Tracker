package uk.co.zac_h.spacex.vehicles.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemRocketPayloadBinding
import uk.co.zac_h.spacex.dto.spacex.PayloadWeights

class RocketPayloadAdapter(private val context: Context) :
    ListAdapter<PayloadWeights, RocketPayloadAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemRocketPayloadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val payload = getItem(position)

        holder.binding.apply {
            listItemRocketPayloadOrbitTypeText.text = payload.name
            listItemRocketPayloadMassText.text = context.getString(
                R.string.mass,
                payload.mass?.kg,
                payload.mass?.lb
            )
        }
    }

    class ViewHolder(val binding: ListItemRocketPayloadBinding) :
        RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<PayloadWeights>() {

        override fun areItemsTheSame(oldItem: PayloadWeights, newItem: PayloadWeights) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: PayloadWeights, newItem: PayloadWeights) =
            oldItem.id == newItem.id
    }
}