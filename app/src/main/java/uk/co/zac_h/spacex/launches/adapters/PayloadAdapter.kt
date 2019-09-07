package uk.co.zac_h.spacex.launches.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.PayloadModel
import uk.co.zac_h.spacex.utils.formatCustomers
import kotlin.math.roundToInt

class PayloadAdapter(private var context: Context?, private var payloads: List<PayloadModel>?) :
    RecyclerView.Adapter<PayloadAdapter.ViewHolder>() {

    private var expandedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_payload,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val payload = payloads?.get(position)

        val isExpanded = position == expandedPosition

        holder.apply {
            details.visibility = if (isExpanded) View.VISIBLE else View.GONE
            itemView.isActivated = isExpanded
            expandIndicator.isChecked = isExpanded

            payloadName.text = payload?.id
            payloadManufacturer.text = payload?.manufacturer

            //Payload specs
            payloadCustomer.text = payload?.customers?.formatCustomers()
            payloadOrbit.text = payload?.orbit
            payloadLifespan.text = payload?.orbitParams?.lifespanYears?.toInt()?.toString() ?: "0"
            payloadMass.text = context?.getString(
                R.string.payload_value,
                payload?.massKg?.roundToInt() ?: 0,
                payload?.massLbs?.roundToInt() ?: 0
            )
            payloadType.text = payload?.type ?: "Unknown"
            payloadRefSystem.text = payload?.orbitParams?.referenceSystem ?: "Unknown"
            payloadRegime.text = payload?.orbitParams?.regime ?: "Unknown"
            payloadLong.text = payload?.orbitParams?.lng?.toString() ?: "0"
            payloadSemiMajor.text = payload?.orbitParams?.semiMajorAxisKm?.toString() ?: "0"
            payloadEccentricity.text = payload?.orbitParams?.eccentricity?.toString() ?: "0"
            payloadPeriapsis.text = payload?.orbitParams?.periapsisKm?.toString() ?: "0"
            payloadApoapsis.text = payload?.orbitParams?.apoapsisKm?.toString() ?: "0"
            payloadInclination.text = payload?.orbitParams?.inclination?.toString() ?: "0"
            payloadPeriod.text = payload?.orbitParams?.periodMins?.toString() ?: "0"

            itemView.setOnClickListener {
                expandedPosition = if (isExpanded) -1 else position
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int = payloads?.size ?: 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val payloadName: TextView = itemView.findViewById(R.id.list_item_payload_name_text)
        val payloadManufacturer: TextView =
            itemView.findViewById(R.id.list_item_payload_manufacturer_text)
        val details: ConstraintLayout = itemView.findViewById(R.id.list_item_payload_details_layout)
        val expandIndicator: ToggleButton =
            itemView.findViewById(R.id.list_item_payload_expand_collapse_toggle)

        //Payload specs
        val payloadCustomer: TextView = itemView.findViewById(R.id.list_item_payload_customer_text)
        val payloadOrbit: TextView = itemView.findViewById(R.id.list_item_payload_orbit_text)
        val payloadLifespan: TextView = itemView.findViewById(R.id.list_item_payload_lifespan_text)
        val payloadMass: TextView = itemView.findViewById(R.id.list_item_payload_mass_text)
        val payloadType: TextView = itemView.findViewById(R.id.list_item_payload_type_text)
        val payloadRefSystem: TextView =
            itemView.findViewById(R.id.list_item_payload_refsystem_text)
        val payloadRegime: TextView = itemView.findViewById(R.id.list_item_payload_regime_text)
        val payloadLong: TextView = itemView.findViewById(R.id.list_item_payload_long_text)
        val payloadSemiMajor: TextView =
            itemView.findViewById(R.id.list_item_payload_semimajor_text)
        val payloadEccentricity: TextView =
            itemView.findViewById(R.id.list_item_payload_eccentricity_text)
        val payloadPeriapsis: TextView =
            itemView.findViewById(R.id.list_item_payload_periapsis_text)
        val payloadApoapsis: TextView = itemView.findViewById(R.id.list_item_payload_apoapsis_text)
        val payloadInclination: TextView =
            itemView.findViewById(R.id.list_item_payload_inclination_text)
        val payloadPeriod: TextView = itemView.findViewById(R.id.list_item_payload_period_text)
    }
}