package uk.co.zac_h.spacex.about.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.about.history.HistoryView
import uk.co.zac_h.spacex.databinding.ListItemHistoryEventBinding
import uk.co.zac_h.spacex.databinding.ListItemHistoryHeadingBinding
import uk.co.zac_h.spacex.utils.animateFromRightWithOffset
import uk.co.zac_h.spacex.utils.animationScaleUpWithOffset
import uk.co.zac_h.spacex.utils.formatDateMillisDDMMM
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel

class HistoryAdapter(
    private var context: Context,
    private var events: ArrayList<HistoryHeaderModel>,
    private var view: HistoryView
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> {
                HeaderViewHolder(
                    ListItemHistoryHeadingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                ViewHolder(
                    ListItemHistoryEventBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = events[position]

        when (holder) {
            is ViewHolder -> with(holder.binding) {
                listItemHistoryLineBottom.visibility =
                    if (position == events.size - 1) View.INVISIBLE else View.VISIBLE

                listItemHistoryMarker.startAnimation(animationScaleUpWithOffset(context, 240))

                listItemHistoryDate.text =
                    event.historyModel?.event?.dateUnix?.formatDateMillisDDMMM()
                listItemHistoryDate.startAnimation(animateFromRightWithOffset(context, 0))

                listItemHistoryTitle.text = event.historyModel?.title
                listItemHistoryTitle.startAnimation(animateFromRightWithOffset(context, 40))

                listItemHistoryDetails.text = event.historyModel?.details
                listItemHistoryDetails.startAnimation(animateFromRightWithOffset(context, 80))

                listItemHistoryArticleButton.apply {
                    event.historyModel?.article?.let { link ->
                        visibility = View.VISIBLE
                        startAnimation(animateFromRightWithOffset(this@HistoryAdapter.context, 240))

                        setOnClickListener {
                            view.openWebLink(link)
                        }
                    } ?: run {
                        visibility = View.GONE
                    }
                }
            }
            is HeaderViewHolder -> with(holder.binding) {
                listItemHistoryLineTop.visibility =
                    if (position == 0) View.INVISIBLE else View.VISIBLE

                listItemHistoryHeading.text = event.header
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

    inner class ViewHolder(val binding: ListItemHistoryEventBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class HeaderViewHolder(val binding: ListItemHistoryHeadingBinding) :
        RecyclerView.ViewHolder(binding.root)
}