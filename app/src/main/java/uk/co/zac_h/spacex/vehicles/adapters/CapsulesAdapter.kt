package uk.co.zac_h.spacex.vehicles.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.Capsule
import java.util.*
import kotlin.collections.ArrayList

class CapsulesAdapter(private val capsules: ArrayList<Capsule>) :
    RecyclerView.Adapter<CapsulesAdapter.ViewHolder>(), Filterable {

    private var filteredCapsules: ArrayList<Capsule>

    init {
        filteredCapsules = capsules
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_capsule,
                parent,
                false
            )
        )

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val capsule = filteredCapsules[position]

        holder.apply {
            itemView.transitionName = capsule.id

            serial.text = capsule.serial
            capsule.type?.let {
                type.text = it.type
            }
            capsule.status?.let {
                status.text = it.status
            }

            capsule.lastUpdate?.let { lastUpdate ->
                details.text = lastUpdate
            } ?: run {
                details.visibility = View.GONE
            }

            flightNumber.text = capsule.reuseCount.toString()

            button.setOnClickListener { bind(capsule) }
            card.setOnClickListener { bind(capsule) }
        }
    }

    override fun getItemCount(): Int = filteredCapsules.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(search: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                search?.let {
                    filteredCapsules = when {
                        it.isEmpty() -> capsules
                        else -> {
                            val filteredList = ArrayList<Capsule>()
                            capsules.forEach { capsule ->
                                capsule.serial?.let { serial ->
                                    if (serial.toLowerCase(Locale.getDefault()).contains(
                                            search.toString().toLowerCase(Locale.getDefault())
                                        )
                                    ) {
                                        filteredList.add(capsule)
                                    }
                                }
                            }
                            filteredList
                        }
                    }
                    filterResults.values = filteredCapsules
                    filterResults.count = filteredCapsules.size
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.list_item_capsule_card)
        val serial: TextView = itemView.findViewById(R.id.list_item_capsule_serial)
        val type: TextView = itemView.findViewById(R.id.list_item_capsule_type_text)
        val status: TextView = itemView.findViewById(R.id.list_item_capsule_status_text)
        val details: TextView = itemView.findViewById(R.id.list_item_capsule_details)
        val flightNumber: TextView = itemView.findViewById(R.id.list_item_capsule_flights_text)
        val button: Button = itemView.findViewById(R.id.list_item_capsule_specs_button)

        fun bind(capsule: Capsule) {
            itemView.findNavController().navigate(
                R.id.action_vehicles_page_fragment_to_capsule_details_fragment,
                bundleOf("capsule" to capsule),
                null,
                FragmentNavigatorExtras(itemView to capsule.id)
            )
        }
    }
}