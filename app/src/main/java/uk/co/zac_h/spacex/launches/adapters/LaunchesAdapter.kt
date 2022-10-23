package uk.co.zac_h.spacex.launches.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemLaunchesBinding
import uk.co.zac_h.spacex.databinding.ListItemLaunchesExpandedBinding
import uk.co.zac_h.spacex.launches.LaunchItem
import uk.co.zac_h.spacex.widget.LaunchView
import kotlin.concurrent.timer

class LaunchesAdapter(val onClick: (LaunchItem, View) -> Unit) :
    ListAdapter<LaunchItem, RecyclerView.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.list_item_launches_expanded -> ExpandedViewHolder(
                ListItemLaunchesExpandedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> ViewHolder(
                ListItemLaunchesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val launch = getItem(position)

        when (holder) {
            is ExpandedViewHolder -> holder.binding.apply {
                root.transitionName = launch.id

                val remaining = launch.countdown()

                countdown.isVisible = remaining != null
                MainScope().launch {
                    withContext(Dispatchers.IO) {
                        timer(period = 1000L) {
                            countdown.text = launch.countdown()
                        }
                    }
                }

                launchView.bind(launch)

                siteNameText.text = launch.launchLocation
                descriptionText.text = launch.description

                root.setOnClickListener {
                    onClick(launch, root)
                }
            }
            is ViewHolder -> holder.binding.apply {
                root.transitionName = launch.id

                launchView.bind(launch)

                root.setOnClickListener {
                    onClick(launch, root)
                }
            }
        }
    }

    private fun LaunchView.bind(launch: LaunchItem) {
        patch = launch.missionPatch
        vehicle = launch.rocket
        missionName = launch.missionName
        date = launch.launchDate

        if (launch.rocket == "Falcon 9") {
            isReused = launch.firstStage?.firstOrNull()?.reused ?: false
            landingPad = launch.firstStage?.firstOrNull()?.landingLocation
        } else {
            isReused = false
            landingPad = null
        }
    }

    override fun getItemViewType(position: Int): Int = if (position == 0) {
        R.layout.list_item_launches_expanded
    } else {
        R.layout.list_item_launches
    }

    class ExpandedViewHolder(val binding: ListItemLaunchesExpandedBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ViewHolder(val binding: ListItemLaunchesBinding) : RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<LaunchItem>() {

        override fun areItemsTheSame(oldItem: LaunchItem, newItem: LaunchItem) = oldItem == newItem

        override fun areContentsTheSame(oldItem: LaunchItem, newItem: LaunchItem) =
            oldItem.id == newItem.id
    }
}
