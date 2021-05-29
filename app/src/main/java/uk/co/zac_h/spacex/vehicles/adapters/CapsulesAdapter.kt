package uk.co.zac_h.spacex.vehicles.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemCapsuleBinding
import uk.co.zac_h.spacex.model.spacex.Capsule

class CapsulesAdapter(private val capsules: ArrayList<Capsule>) :
    RecyclerView.Adapter<CapsulesAdapter.ViewHolder>(), Filterable {

    private var filteredCapsules: ArrayList<Capsule>

    init {
        filteredCapsules = capsules
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemCapsuleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val capsule = filteredCapsules[position]

        holder.binding.apply {
            listItemCapsuleCard.transitionName = capsule.id

            listItemCapsuleSerial.text = capsule.serial
            capsule.type?.let {
                listItemCapsuleTypeText.text = it.type
            }
            capsule.status?.let {
                listItemCapsuleStatusText.text = it.status
            }

            capsule.lastUpdate?.let { lastUpdate ->
                listItemCapsuleDetails.text = lastUpdate
            } ?: run {
                listItemCapsuleDetails.visibility = View.GONE
            }

            listItemCapsuleFlightsText.text = capsule.reuseCount.toString()

            listItemCapsuleSpecsButton.setOnClickListener { holder.bind(capsule) }
            listItemCapsuleCard.setOnClickListener { holder.bind(capsule) }
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
                                    if (serial.lowercase()
                                            .contains(search.toString().lowercase())
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

    class ViewHolder(val binding: ListItemCapsuleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(capsule: Capsule) {
            itemView.findNavController().navigate(
                R.id.action_vehicles_page_fragment_to_capsule_details_fragment,
                bundleOf("capsule" to capsule),
                null,
                FragmentNavigatorExtras(binding.listItemCapsuleCard to capsule.id)
            )
        }
    }
}