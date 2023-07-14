package uk.co.zac_h.spacex.feature.subscription

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.core.common.recyclerview.RecyclerViewItem
import uk.co.zac_h.spacex.feature.subscription.databinding.ListItemCategoryBinding
import uk.co.zac_h.spacex.feature.subscription.databinding.ListItemSubscriptionBinding
import uk.co.zac_h.spacex.feature.subscription.databinding.ListItemSubscriptionCurrentBinding

class SubscriptionsAdapter(val callback: SubscriptionsCallback) :
    ListAdapter<RecyclerViewItem, RecyclerView.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.list_item_subscription_current -> CurrentSubscriptionViewHolder(
                ListItemSubscriptionCurrentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            R.layout.list_item_subscription -> SubscriptionViewHolder(
                ListItemSubscriptionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            R.layout.list_item_category -> CategoryViewHolder(
                ListItemCategoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> error("Invalid viewType: $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is CurrentSubscriptionViewHolder -> holder.binding.apply {
                item as SubscriptionPlanItem

                planName.text = item.name
                planCost.text =
                    root.context.getString(R.string.cost_per_month, item.monthlyCost.toString())
                planRenewalDate.text = item.renewalDate
                planDetails.text = item.details.joinToString("\n")
                planManageButton.isVisible = item.id != 0

                planManageButton.setOnClickListener {
                    callback.unsubscribe()
                }
            }

            is SubscriptionViewHolder -> holder.binding.apply {
                item as SubscriptionPlanItem

                planName.text = item.name
                planCost.text =
                    root.context.getString(R.string.cost_per_month, item.monthlyCost.toString())

                planManageButton.setOnClickListener {
                    callback.subscribe()
                }
            }

            is CategoryViewHolder -> holder.binding.apply {
                item as CategoryItem

                name.text = item.name
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (val item = getItem(position)) {
        is SubscriptionPlanItem -> when (item.isCurrentPlan) {
            true -> R.layout.list_item_subscription_current
            false -> R.layout.list_item_subscription
        }

        is CategoryItem -> R.layout.list_item_category
        else -> error("Invalid item type $item")
    }

    class CurrentSubscriptionViewHolder(val binding: ListItemSubscriptionCurrentBinding) :
        RecyclerView.ViewHolder(binding.root)

    class SubscriptionViewHolder(val binding: ListItemSubscriptionBinding) :
        RecyclerView.ViewHolder(binding.root)

    class CategoryViewHolder(val binding: ListItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<RecyclerViewItem>() {

        override fun areItemsTheSame(
            oldItem: RecyclerViewItem,
            newItem: RecyclerViewItem
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: RecyclerViewItem,
            newItem: RecyclerViewItem
        ) = oldItem == newItem
    }
}
