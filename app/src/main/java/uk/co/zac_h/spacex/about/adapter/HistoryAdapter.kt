package uk.co.zac_h.spacex.about.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.HistoryHeaderModel
import uk.co.zac_h.spacex.utils.formatDateMillisDDMMM

class HistoryAdapter(private var events: ArrayList<HistoryHeaderModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> {
                HeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_history_heading,
                        parent,
                        false
                    )
                )
            }
            else -> {
                ViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_history_event,
                        parent,
                        false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = events[position]

        when (holder) {
            is ViewHolder -> holder.apply {
                date.text = event.historyModel?.dateUnix?.formatDateMillisDDMMM()
                title.text = event.historyModel?.title
                details.text = event.historyModel?.details
            }
            is HeaderViewHolder -> holder.heading.text = event.header
        }
    }

    fun isHeader(): (itemPosition: Int) -> Boolean {
        return {
            events[it].isHeader
        }
    }

    override fun getItemCount(): Int = events.size

    override fun getItemViewType(position: Int): Int = if (events[position].isHeader) 0 else 1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.list_item_history_date)
        val title: TextView = itemView.findViewById(R.id.list_item_history_title)
        val details: TextView = itemView.findViewById(R.id.list_item_history_details)
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val heading: TextView = itemView.findViewById(R.id.list_item_history_heading)
    }
}