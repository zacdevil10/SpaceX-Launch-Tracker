package uk.co.zac_h.spacex.about.adapter

import android.animation.LayoutTransition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.HistoryHeaderModel
import uk.co.zac_h.spacex.utils.formatDateMillisDDMMM

class HistoryAdapter(private var events: ArrayList<HistoryHeaderModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var expandedPosition = -1

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
                val isExpanded = position == expandedPosition

                lineBottom.visibility =
                    if (position == events.size - 1) View.INVISIBLE else View.VISIBLE

                details.visibility = if (isExpanded) View.VISIBLE else View.GONE

                root.layoutTransition.disableTransitionType(LayoutTransition.CHANGING)

                date.text = event.historyModel?.dateUnix?.formatDateMillisDDMMM()
                title.text = event.historyModel?.title
                details.text = event.historyModel?.details

                event.historyModel?.flightNumber?.let { flight ->
                    launchDetailsButton.setOnClickListener {
                        itemView.findNavController()
                            .navigate(
                                R.id.action_history_fragment_to_launch_details_fragment,
                                bundleOf(
                                    "launch_id" to flight.toString()
                                )
                            )
                    }
                }

                launchDetailsButton.visibility =
                    event.historyModel?.flightNumber?.let { View.VISIBLE } ?: View.GONE

                card.setOnClickListener {
                    expandedPosition = if (isExpanded) -1 else position
                    root.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                    notifyItemChanged(position)
                }
            }
            is HeaderViewHolder -> holder.apply {
                lineTop.visibility =
                    if (position == 0) View.INVISIBLE else View.VISIBLE

                heading.text = event.header
            }
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
        val card: CardView = itemView.findViewById(R.id.list_item_history_card)
        val date: TextView = itemView.findViewById(R.id.list_item_history_date)
        val title: TextView = itemView.findViewById(R.id.list_item_history_title)
        val details: TextView = itemView.findViewById(R.id.list_item_history_details)
        val launchDetailsButton: TextView =
            itemView.findViewById(R.id.list_item_history_details_button)

        val lineBottom: View = itemView.findViewById(R.id.list_item_history_line_bottom)

        val root: ConstraintLayout = itemView.findViewById(R.id.root)
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val heading: TextView = itemView.findViewById(R.id.list_item_history_heading)

        val lineTop: View = itemView.findViewById(R.id.list_item_history_line_top)
    }
}