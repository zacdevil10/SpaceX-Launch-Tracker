package uk.co.zac_h.spacex.about.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.databinding.ListItemHistoryEventBinding
import uk.co.zac_h.spacex.databinding.ListItemHistoryHeadingBinding
import uk.co.zac_h.spacex.utils.animateFromRightWithOffset
import uk.co.zac_h.spacex.utils.animationScaleUpWithOffset
import uk.co.zac_h.spacex.utils.formatDateMillisDDMMM
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel

class HistoryAdapter(
    private var context: Context,
    private var openWebLink: (String) -> Unit
) : ListAdapter<HistoryHeaderModel, RecyclerView.ViewHolder>(HistoryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> HeaderViewHolder(
                ListItemHistoryHeadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> ViewHolder(
                ListItemHistoryEventBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = getItem(position)

        when (holder) {
            is ViewHolder -> with(holder.binding) {
                lineBottom.visibility =
                    if (position == itemCount.minus(1)) View.INVISIBLE else View.VISIBLE

                marker.startAnimation(animationScaleUpWithOffset(context))

                date.text =
                    event.historyModel?.event?.dateUnix?.formatDateMillisDDMMM()
                date.startAnimation(animateFromRightWithOffset(context, 0))

                title.text = event.historyModel?.title
                title.startAnimation(animateFromRightWithOffset(context, 40))

                details.text = event.historyModel?.details
                details.startAnimation(animateFromRightWithOffset(context, 80))

                article.apply {
                    event.historyModel?.article?.let { link ->
                        visibility = View.VISIBLE
                        startAnimation(animateFromRightWithOffset(this@HistoryAdapter.context, 240))

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

    override fun getItemViewType(position: Int): Int = if (getItem(position).isHeader) 0 else 1

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