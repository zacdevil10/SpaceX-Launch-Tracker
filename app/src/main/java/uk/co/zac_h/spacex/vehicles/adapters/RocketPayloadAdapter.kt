package uk.co.zac_h.spacex.vehicles.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemRocketPayloadBinding
import uk.co.zac_h.spacex.dto.spacex.PayloadWeights

class RocketPayloadAdapter(
    private val context: Context,
    private val payloads: List<PayloadWeights>
) : RecyclerView.Adapter<RocketPayloadAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemRocketPayloadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val payload = payloads[position]

        holder.binding.apply {
            listItemRocketPayloadOrbitTypeText.text = payload.name
            listItemRocketPayloadMassText.text = context.getString(
                R.string.mass,
                payload.mass?.kg,
                payload.mass?.lb
            )
        }
    }

    override fun getItemCount(): Int = payloads.size

    class ViewHolder(val binding: ListItemRocketPayloadBinding) :
        RecyclerView.ViewHolder(binding.root)
}