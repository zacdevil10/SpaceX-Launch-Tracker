package uk.co.zac_h.spacex.launches.adapters

import android.animation.LayoutTransition
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.PayloadModel
import uk.co.zac_h.spacex.utils.formatCustomers
import uk.co.zac_h.spacex.utils.metricFormat

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

            root.layoutTransition.disableTransitionType(LayoutTransition.CHANGING)

            payloadName.text = payload?.id
            payloadManufacturer.text = payload?.manufacturers?.formatCustomers()

            //Payload specs
            payloadCustomer.text = payload?.customers?.formatCustomers()
            payloadOrbit.text = payload?.orbit

            payload?.lifespan?.let {
                payloadLifespan.text = it.toInt().toString()
            } ?: run {
                payloadLifespanLabel.visibility = View.GONE
                payloadLifespan.visibility = View.GONE
            }

            payloadMass.text = context?.getString(
                R.string.mass,
                payload?.massKg?.metricFormat() ?: 0,
                payload?.massLbs?.metricFormat() ?: 0
            )

            payload?.type?.let {
                payloadType.text = it
            } ?: run {
                payloadType.visibility = View.GONE
                payloadTypeLabel.visibility = View.GONE
            }

            payload?.referenceSystem?.let {
                payloadRefSystem.text = it
            } ?: run {
                payloadRefSystem.visibility = View.GONE
                payloadRefSystemLabel.visibility = View.GONE
            }

            payload?.regime?.let {
                payloadRegime.text = it
            } ?: run {
                payloadRegime.visibility = View.GONE
                payloadRegimeLabel.visibility = View.GONE
            }

            payload?.longitude?.let {
                payloadLong.text = it.toString()
            } ?: run {
                payloadLong.visibility = View.GONE
                payloadLongLabel.visibility = View.GONE
            }

            payload?.semiMajorAxisKm?.let {
                payloadSemiMajor.text = it.toString()
            } ?: run {
                payloadSemiMajor.visibility = View.GONE
                payloadSemiMajorLabel.visibility = View.GONE
            }

            payloadEccentricity.text = payload?.eccentricity?.toString() ?: "0"
            payload?.eccentricity?.let {
                payloadEccentricity.text = it.toString()
            } ?: run {
                payloadEccentricity.visibility = View.GONE
                payloadEccentricityLabel.visibility = View.GONE
            }

            payload?.periapsisKm?.let {
                payloadPeriapsis.text = it.toString()
            } ?: run {
                payloadPeriapsis.visibility = View.GONE
                payloadPeriapsisLabel.visibility = View.GONE
            }

            payload?.apoapsisKm?.let {
                payloadApoapsis.text = it.toString()
            } ?: run {
                payloadApoapsis.visibility = View.GONE
                payloadApoapsisLabel.visibility = View.GONE
            }

            payload?.inclination?.let {
                payloadInclination.text = it.toString()
            } ?: run {
                payloadInclination.visibility = View.GONE
                payloadInclinationLabel.visibility = View.GONE
            }

            payload?.period?.let {
                payloadPeriod.text = it.toString()
            } ?: run {
                payloadPeriod.visibility = View.GONE
                payloadPeriodLabel.visibility = View.GONE
            }

            itemView.setOnClickListener {
                expandedPosition = if (isExpanded) -1 else position
                root.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int = payloads?.size ?: 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root: ConstraintLayout = itemView.findViewById(R.id.root)

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

        //Payload labels
        val payloadLifespanLabel: TextView =
            itemView.findViewById(R.id.list_item_payload_lifespan_label)
        val payloadTypeLabel: TextView = itemView.findViewById(R.id.list_item_payload_type_label)
        val payloadRefSystemLabel: TextView =
            itemView.findViewById(R.id.list_item_payload_refsystem_label)
        val payloadRegimeLabel: TextView =
            itemView.findViewById(R.id.list_item_payload_regime_label)
        val payloadLongLabel: TextView = itemView.findViewById(R.id.list_item_payload_long_label)
        val payloadSemiMajorLabel: TextView =
            itemView.findViewById(R.id.list_item_payload_semimajor_label)
        val payloadEccentricityLabel: TextView =
            itemView.findViewById(R.id.list_item_payload_eccentricity_label)
        val payloadPeriapsisLabel: TextView =
            itemView.findViewById(R.id.list_item_payload_periapsis_label)
        val payloadApoapsisLabel: TextView =
            itemView.findViewById(R.id.list_item_payload_apoapsis_label)
        val payloadInclinationLabel: TextView =
            itemView.findViewById(R.id.list_item_payload_inclination_label)
        val payloadPeriodLabel: TextView =
            itemView.findViewById(R.id.list_item_payload_period_label)
    }
}