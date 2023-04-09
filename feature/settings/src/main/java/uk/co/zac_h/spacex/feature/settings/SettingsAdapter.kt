package uk.co.zac_h.spacex.feature.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.core.common.recyclerview.RecyclerViewItem
import uk.co.zac_h.spacex.feature.settings.databinding.ListItemSettingsHeaderBinding
import uk.co.zac_h.spacex.feature.settings.databinding.ListItemSettingsLinkBinding

class SettingsAdapter : ListAdapter<RecyclerViewItem, RecyclerView.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.list_item_settings_link -> SettingsLinkViewHolder(
                ListItemSettingsLinkBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            R.layout.list_item_settings_header -> SettingsHeaderViewHolder(
                ListItemSettingsHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw IllegalStateException()
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is SettingsHeaderViewHolder -> holder.binding.apply {
                item as SettingsHeader

                headerText.text = item.label
            }

            is SettingsLinkViewHolder -> holder.binding.apply {
                item as SettingsItem

                icon.setImageResource(item.icon)
                title.text = item.label
                root.setOnClickListener {
                    it.findNavController().navigate(item.direction)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is SettingsHeader -> R.layout.list_item_settings_header
        is SettingsItem -> R.layout.list_item_settings_link
        else -> throw IllegalStateException()
    }

    internal class SettingsHeaderViewHolder(val binding: ListItemSettingsHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    internal class SettingsLinkViewHolder(val binding: ListItemSettingsLinkBinding) :
        RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<RecyclerViewItem>() {

        override fun areItemsTheSame(oldItem: RecyclerViewItem, newItem: RecyclerViewItem) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: RecyclerViewItem, newItem: RecyclerViewItem) =
            if (oldItem is SettingsItem && newItem is SettingsItem) {
                oldItem == newItem
            } else if (oldItem is SettingsHeader && newItem is SettingsHeader) {
                oldItem == newItem
            } else {
                oldItem == newItem
            }
    }
}