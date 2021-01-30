package uk.co.zac_h.spacex.launches.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemPayloadBinding
import uk.co.zac_h.spacex.model.spacex.Payload
import uk.co.zac_h.spacex.utils.formatCustomers

class PayloadAdapter(private var context: Context?, private var payloads: List<Payload>?) :
    RecyclerView.Adapter<PayloadAdapter.ViewHolder>() {

    private var expandedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemPayloadBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val payload = payloads?.get(position)

        val isExpanded = position == expandedPosition

        holder.binding.apply {
            listItemPayloadDetailsLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
            root.isActivated = isExpanded
            listItemPayloadExpandCollapseToggle.isChecked = isExpanded

            listItemPayloadNameText.text = payload?.name
            listItemPayloadManufacturerText.text = payload?.manufacturers?.formatCustomers()

            //Payload specs
            listItemPayloadCustomerText.text = payload?.customers?.formatCustomers()
            listItemPayloadOrbitText.text = payload?.orbit

            payload?.lifespan?.let {
                listItemPayloadLifespanText.text = it.toString()
            } ?: run {
                listItemPayloadLifespanLabel.visibility = View.GONE
                listItemPayloadLifespanText.visibility = View.GONE
            }

            payload?.formattedMass?.let {
                listItemPayloadMassText.text = context?.getString(
                    R.string.mass,
                    it.kg,
                    it.lb
                )
            } ?: run {
                listItemPayloadMassText.visibility = View.GONE
                listItemPayloadMassLabel.visibility = View.GONE
            }

            payload?.type?.let {
                listItemPayloadTypeText.text = it
            } ?: run {
                listItemPayloadTypeText.visibility = View.GONE
                listItemPayloadTypeLabel.visibility = View.GONE
            }

            payload?.referenceSystem?.let {
                listItemPayloadRefsystemText.text = it
            } ?: run {
                listItemPayloadRefsystemText.visibility = View.GONE
                listItemPayloadRefsystemLabel.visibility = View.GONE
            }

            payload?.regime?.let {
                listItemPayloadRegimeText.text = it
            } ?: run {
                listItemPayloadRegimeText.visibility = View.GONE
                listItemPayloadRegimeLabel.visibility = View.GONE
            }

            payload?.longitude?.let {
                listItemPayloadLongText.text = it.toString()
            } ?: run {
                listItemPayloadLongText.visibility = View.GONE
                listItemPayloadLongLabel.visibility = View.GONE
            }

            payload?.semiMajorAxisKm?.let {
                listItemPayloadSemimajorText.text = it.toString()
            } ?: run {
                listItemPayloadSemimajorText.visibility = View.GONE
                listItemPayloadSemimajorLabel.visibility = View.GONE
            }

            payload?.eccentricity?.let {
                listItemPayloadEccentricityText.text = it.toString()
            } ?: run {
                listItemPayloadEccentricityText.visibility = View.GONE
                listItemPayloadEccentricityLabel.visibility = View.GONE
            }

            payload?.periapsisKm?.let {
                listItemPayloadPeriapsisText.text = it.toString()
            } ?: run {
                listItemPayloadPeriapsisText.visibility = View.GONE
                listItemPayloadPeriapsisLabel.visibility = View.GONE
            }

            payload?.apoapsisKm?.let {
                listItemPayloadApoapsisText.text = it.toString()
            } ?: run {
                listItemPayloadApoapsisText.visibility = View.GONE
                listItemPayloadApoapsisLabel.visibility = View.GONE
            }

            payload?.inclination?.let {
                listItemPayloadInclinationText.text = it.toString()
            } ?: run {
                listItemPayloadInclinationText.visibility = View.GONE
                listItemPayloadInclinationLabel.visibility = View.GONE
            }

            payload?.period?.let {
                listItemPayloadPeriodText.text = it.toString()
            } ?: run {
                listItemPayloadPeriodText.visibility = View.GONE
                listItemPayloadPeriodLabel.visibility = View.GONE
            }

            root.setOnClickListener {
                expandedPosition = if (isExpanded) -1 else position
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int = payloads?.size ?: 0

    class ViewHolder(val binding: ListItemPayloadBinding) : RecyclerView.ViewHolder(binding.root)
}