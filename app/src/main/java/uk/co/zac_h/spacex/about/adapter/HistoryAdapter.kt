package uk.co.zac_h.spacex.about.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.about.history.HistoryView
import uk.co.zac_h.spacex.utils.HistoryHeaderModel
import uk.co.zac_h.spacex.utils.formatDateMillisDDMMM

class HistoryAdapter(
    private var events: ArrayList<HistoryHeaderModel>,
    private var view: HistoryView
) :
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
                lineBottom.visibility =
                    if (position == events.size - 1) View.INVISIBLE else View.VISIBLE

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
                redditButton.visibility =
                    event.historyModel?.links?.reddit?.let { View.VISIBLE } ?: View.GONE
                wikiButton.visibility =
                    event.historyModel?.links?.wikipedia?.let { View.VISIBLE } ?: View.GONE
                articleButton.visibility =
                    event.historyModel?.links?.article?.let { View.VISIBLE } ?: View.GONE

                redditButton.setOnClickListener {
                    event.historyModel?.links?.reddit?.let { link -> view.openWebLink(link) }
                }

                wikiButton.setOnClickListener {
                    event.historyModel?.links?.wikipedia?.let { link -> view.openWebLink(link) }
                }

                articleButton.setOnClickListener {
                    event.historyModel?.links?.article?.let { link -> view.openWebLink(link) }
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
        val date: TextView = itemView.findViewById(R.id.list_item_history_date)
        val title: TextView = itemView.findViewById(R.id.list_item_history_title)
        val details: TextView = itemView.findViewById(R.id.list_item_history_details)
        val launchDetailsButton: ConstraintLayout =
            itemView.findViewById(R.id.list_item_history_details_button)
        val redditButton: ConstraintLayout =
            itemView.findViewById(R.id.list_item_history_reddit_button)
        val wikiButton: ConstraintLayout =
            itemView.findViewById(R.id.list_item_history_wiki_button)
        val articleButton: ConstraintLayout =
            itemView.findViewById(R.id.list_item_history_article_button)

        val lineBottom: View = itemView.findViewById(R.id.list_item_history_line_bottom)
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val heading: TextView = itemView.findViewById(R.id.list_item_history_heading)

        val lineTop: View = itemView.findViewById(R.id.list_item_history_line_top)
    }
}