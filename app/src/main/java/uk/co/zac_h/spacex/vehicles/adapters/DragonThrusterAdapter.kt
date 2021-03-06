package uk.co.zac_h.spacex.vehicles.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemDragonThrusterBinding
import uk.co.zac_h.spacex.model.spacex.DragonThrusterConfiguration
import uk.co.zac_h.spacex.utils.metricFormat
import java.util.*

class DragonThrusterAdapter(
    private val context: Context?,
    private val thrusters: List<DragonThrusterConfiguration>
) : RecyclerView.Adapter<DragonThrusterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemDragonThrusterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    @ExperimentalStdlibApi
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val thruster = thrusters[position]

        holder.binding.apply {
            listItemDragonThrusterTypeText.text = thruster.type
            listItemDragonThrusterAmountText.text = thruster.amount.toString()
            listItemDragonThrusterPodsText.text = thruster.pods.toString()
            listItemDragonThrusterFuelOneText.text = thruster.fuelType1?.capitalize(Locale.ENGLISH)
            listItemDragonThrusterFuelTwoText.text = thruster.fuelType2?.capitalize(Locale.ENGLISH)
            listItemDragonThrusterThrustText.text = context?.getString(
                R.string.thrust,
                thruster.thrust?.kN?.metricFormat(),
                thruster.thrust?.lbf?.metricFormat()
            )
        }
    }

    override fun getItemCount(): Int = thrusters.size

    class ViewHolder(val binding: ListItemDragonThrusterBinding) :
        RecyclerView.ViewHolder(binding.root)

}