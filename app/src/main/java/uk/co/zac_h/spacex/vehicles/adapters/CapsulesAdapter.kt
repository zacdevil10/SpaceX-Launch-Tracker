package uk.co.zac_h.spacex.vehicles.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.CapsulesModel
import uk.co.zac_h.spacex.utils.formatDateMillisShort

class CapsulesAdapter(private val capsules: List<CapsulesModel>) :
    RecyclerView.Adapter<CapsulesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_capsule,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val capsule = capsules[position]

        holder.apply {
            serial.text = capsule.serial
            type.text = capsule.type
            status.text = capsule.status.capitalize()
            date.text = capsule.originalLaunchUnix?.formatDateMillisShort() ?: "TBD"
        }
    }

    override fun getItemCount(): Int = capsules.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serial: TextView = itemView.findViewById(R.id.list_item_capsule_serial)
        val type: TextView = itemView.findViewById(R.id.list_item_capsule_type)
        val status: TextView = itemView.findViewById(R.id.list_item_capsule_status_text)
        val date: TextView = itemView.findViewById(R.id.list_item_capsule_date_text)
    }
}