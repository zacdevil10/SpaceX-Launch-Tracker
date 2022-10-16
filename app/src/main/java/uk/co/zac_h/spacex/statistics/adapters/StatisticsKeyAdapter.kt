package uk.co.zac_h.spacex.statistics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.core.utils.metricFormat
import uk.co.zac_h.spacex.databinding.ListItemKeyBinding
import uk.co.zac_h.spacex.utils.models.KeysModel

class StatisticsKeyAdapter(
    private val context: Context,
    private val format: Boolean
) : ListAdapter<KeysModel, StatisticsKeyAdapter.ViewHolder>(StatisticsKeyComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemKeyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = getItem(position)

        holder.binding.apply {
            listItemKeyLabel.text = key.label
            listItemKeyValue.text = if (format) context.getString(
                R.string.mass_kg,
                key.value.metricFormat()
            ) else key.value.toInt().toString()
        }
    }

    class ViewHolder(val binding: ListItemKeyBinding) : RecyclerView.ViewHolder(binding.root)

    object StatisticsKeyComparator : DiffUtil.ItemCallback<KeysModel>() {

        override fun areItemsTheSame(oldItem: KeysModel, newItem: KeysModel) = oldItem == newItem

        override fun areContentsTheSame(oldItem: KeysModel, newItem: KeysModel) =
            oldItem.label == newItem.label && oldItem.value == newItem.value

    }
}
