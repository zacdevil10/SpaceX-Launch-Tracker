package uk.co.zac_h.spacex.vehicles.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.ThrusterConfigModel
import uk.co.zac_h.spacex.utils.metricFormat

class DragonThrusterAdapter(
    private val context: Context?,
    private val thrusters: List<ThrusterConfigModel>
) : RecyclerView.Adapter<DragonThrusterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_dragon_thruster,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val thruster = thrusters[position]

        holder.apply {
            type.text = thruster.type
            amount.text = thruster.amount.toString()
            pods.text = thruster.pods.toString()
            fuel1.text = thruster.fuelType1.capitalize()
            fuel2.text = thruster.fuelType2.capitalize()
            thrust.text = context?.getString(
                R.string.thrust,
                thruster.thrust.kN.metricFormat(),
                thruster.thrust.lbf.metricFormat()
            )
        }
    }

    override fun getItemCount(): Int = thrusters.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val type: TextView = itemView.findViewById(R.id.list_item_dragon_thruster_type_text)
        val amount: TextView = itemView.findViewById(R.id.list_item_dragon_thruster_amount_text)
        val pods: TextView = itemView.findViewById(R.id.list_item_dragon_thruster_pods_text)
        val fuel1: TextView = itemView.findViewById(R.id.list_item_dragon_thruster_fuel_one_text)
        val fuel2: TextView = itemView.findViewById(R.id.list_item_dragon_thruster_fuel_two_text)
        val thrust: TextView = itemView.findViewById(R.id.list_item_dragon_thruster_thrust_text)
    }

}