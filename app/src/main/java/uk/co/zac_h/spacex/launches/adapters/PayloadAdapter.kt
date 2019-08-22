package uk.co.zac_h.spacex.launches.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.data.PayloadModel

class PayloadAdapter(private var payloads: List<PayloadModel>?) : RecyclerView.Adapter<PayloadAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_payload, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val payload = payloads?.get(position)

        holder.apply {
            payloadName.text = payload?.id
            payloadOrbit.text = payload?.orbit
            payloadManufacturer.text = payload?.manufacturer
        }
    }

    override fun getItemCount(): Int = payloads?.size ?: 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val payloadName: TextView = itemView.findViewById(R.id.list_item_payload_name_text)
        val payloadOrbit: TextView = itemView.findViewById(R.id.list_item_payload_orbit_text)
        val payloadManufacturer: TextView =
            itemView.findViewById(R.id.list_item_payload_manufacturer_text)
    }
}