package uk.co.zac_h.spacex.statistics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.metricFormat
import uk.co.zac_h.spacex.utils.models.KeysModel

class StatisticsKeyAdapter(private val context: Context?, private var keys: ArrayList<KeysModel>) :
    RecyclerView.Adapter<StatisticsKeyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_key,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = keys[position]

        holder.apply {
            label.text = key.label
            value.text = context?.getString(R.string.mass_kg, key.value.metricFormat())
        }
    }

    override fun getItemCount(): Int = keys.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var label: TextView = itemView.findViewById(R.id.list_item_key_label)
        val value: TextView = itemView.findViewById(R.id.list_item_key_value)
    }

}