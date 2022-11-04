package uk.co.zac_h.spacex.about.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.core.common.utils.formatDateMillisDDMMM
import uk.co.zac_h.spacex.databinding.ListItemHistoryEventBinding
import uk.co.zac_h.spacex.databinding.ListItemHistoryHeadingBinding
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel

class HistoryAdapter(
    private var openWebLink: (String) -> Unit
) : ListAdapter<HistoryHeaderModel, RecyclerView.ViewHolder>(HistoryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.list_item_history_heading -> HeaderViewHolder(
                ListItemHistoryHeadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.list_item_history_event -> ViewHolder(
                ListItemHistoryEventBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid layout type for history adapter")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = getItem(position)

        when (holder) {
            is ViewHolder -> with(holder.binding) {
                lineBottom.visibility =
                    if (position == itemCount.minus(1)) View.INVISIBLE else View.VISIBLE

                date.text = event.historyModel?.event?.dateUnix?.formatDateMillisDDMMM()
                title.text = event.historyModel?.title
                details.text = event.historyModel?.details

                article.apply {
                    event.historyModel?.article?.let { link ->
                        visibility = View.VISIBLE

                        setOnClickListener {
                            openWebLink(link)
                        }
                    } ?: run {
                        visibility = View.GONE
                    }
                }
            }
            is HeaderViewHolder -> with(holder.binding) {
                lineTop.visibility =
                    if (position == 0) View.INVISIBLE else View.VISIBLE

                heading.text = event.header
            }
        }
    }

    fun isHeader(): (itemPosition: Int) -> Boolean = { getItem(it).isHeader }

    override fun getItemViewType(position: Int): Int = if (getItem(position).isHeader) {
        R.layout.list_item_history_heading
    } else {
        R.layout.list_item_history_event
    }

    inner class ViewHolder(val binding: ListItemHistoryEventBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class HeaderViewHolder(val binding: ListItemHistoryHeadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    object HistoryComparator : DiffUtil.ItemCallback<HistoryHeaderModel>() {

        override fun areItemsTheSame(
            oldItem: HistoryHeaderModel,
            newItem: HistoryHeaderModel
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: HistoryHeaderModel,
            newItem: HistoryHeaderModel
        ) = oldItem.isHeader == newItem.isHeader
                && oldItem.header == newItem.header
                && oldItem.historyModel?.id == newItem.historyModel?.id

    }
}