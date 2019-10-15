package uk.co.zac_h.spacex.vehicles.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.PayloadWeightsModel
import uk.co.zac_h.spacex.utils.metricFormat

class RocketPayloadAdapter(
    private val context: Context?,
    private val payloads: List<PayloadWeightsModel>
) : RecyclerView.Adapter<RocketPayloadAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_rocket_payload,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val payload = payloads[position]

        holder.apply {
            orbit.text = payload.name
            mass.text = context?.getString(
                R.string.mass,
                payload.kg.metricFormat(),
                payload.lb.metricFormat()
            )
        }
    }

    override fun getItemCount(): Int = payloads.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orbit: TextView = itemView.findViewById(R.id.list_item_rocket_payload_orbit_text)
        val mass: TextView = itemView.findViewById(R.id.list_item_rocket_payload_mass_text)
    }
}