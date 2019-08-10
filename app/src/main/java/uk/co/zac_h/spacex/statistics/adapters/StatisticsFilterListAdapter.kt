package uk.co.zac_h.spacex.statistics.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.statistics.graphs.GraphsView

class StatisticsFilterListAdapter(private val view: GraphsView, private val labels: Array<String>) : RecyclerView.Adapter<StatisticsFilterListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_statistics_filter, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            label.text = labels[position]

            itemView.setOnClickListener {
                checkBox.isChecked = !checkBox.isChecked
            }

            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                when (position) {
                    0 -> view.updateLaunchesList("success", !isChecked)
                    1 -> view.updateLaunchesList("failed", !isChecked)
                }
            }
        }
    }

    override fun getItemCount(): Int = labels.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val label: TextView = itemView.findViewById(R.id.filter_label_text)
        val checkBox: CheckBox = itemView.findViewById(R.id.filter_checkbox)
    }
}