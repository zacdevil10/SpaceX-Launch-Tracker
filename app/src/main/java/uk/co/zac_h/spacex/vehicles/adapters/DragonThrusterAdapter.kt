package uk.co.zac_h.spacex.vehicles.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.core.utils.metricFormat
import uk.co.zac_h.spacex.databinding.ListItemDragonThrusterBinding
import uk.co.zac_h.spacex.network.dto.spacex.DragonThrusterConfiguration

class DragonThrusterAdapter(private val context: Context) :
    ListAdapter<DragonThrusterConfiguration, DragonThrusterAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemDragonThrusterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val thruster = getItem(position)

        holder.binding.apply {
            listItemDragonThrusterTypeText.text = thruster.type
            listItemDragonThrusterAmountText.text = thruster.amount.toString()
            listItemDragonThrusterPodsText.text = thruster.pods.toString()
            listItemDragonThrusterFuelOneText.text =
                thruster.fuelType1?.replaceFirstChar { it.uppercase() }
            listItemDragonThrusterFuelTwoText.text =
                thruster.fuelType2?.replaceFirstChar { it.uppercase() }
            listItemDragonThrusterThrustText.text = context.getString(
                R.string.thrust,
                thruster.thrust?.kN?.metricFormat(),
                thruster.thrust?.lbf?.metricFormat()
            )
        }
    }

    class ViewHolder(val binding: ListItemDragonThrusterBinding) :
        RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<DragonThrusterConfiguration>() {

        override fun areItemsTheSame(
            oldItem: DragonThrusterConfiguration,
            newItem: DragonThrusterConfiguration
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: DragonThrusterConfiguration,
            newItem: DragonThrusterConfiguration
        ) = oldItem.type == newItem.type
    }
}
