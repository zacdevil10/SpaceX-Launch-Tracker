package uk.co.zac_h.spacex.vehicles.adapters

import android.content.Context
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
import uk.co.zac_h.spacex.databinding.ListItemCoreBinding
import uk.co.zac_h.spacex.dto.spacex.Core

class CoreAdapter(private val context: Context, private val cores: ArrayList<Core>) :
    RecyclerView.Adapter<CoreAdapter.ViewHolder>(), Filterable {

    private var filteredCores: ArrayList<Core>

    init {
        filteredCores = cores
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemCoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val core = filteredCores[position]

        holder.binding.apply {
            listItemCoreCard.transitionName = core.id

            listItemCoreSerial.text = core.serial

            core.block?.let {
                listItemCoreBlockText.text = context.getString(R.string.block, it)
                listItemCoreTitleSeparator.visibility = View.VISIBLE
            } ?: run {
                listItemCoreBlockText.text = ""
                listItemCoreTitleSeparator.visibility = View.GONE
            }

            core.lastUpdate?.let {
                listItemCoreDetails.text = it
                listItemCoreDetails.visibility = View.VISIBLE
            } ?: run {
                listItemCoreDetails.visibility = View.GONE
            }
            core.status?.let {
                listItemCoreStatusText.text = it.status
            }
            listItemCoreFlightsText.text = (core.launches?.size ?: 0).toString()

            listItemCoreSpecsButton.setOnClickListener { holder.bind(core) }
            listItemCoreCard.setOnClickListener { holder.bind(core) }
        }
    }

    override fun getItemCount(): Int = filteredCores.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(search: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                search?.let {
                    filteredCores = when {
                        it.isEmpty() -> cores
                        else -> {
                            val filteredList = ArrayList<Core>()
                            cores.forEach { core ->
                                core.serial?.let { serial ->
                                    if (serial.lowercase()
                                            .contains(search.toString().lowercase())
                                    ) {
                                        filteredList.add(core)
                                    }
                                }
                            }
                            filteredList
                        }
                    }
                    filterResults.values = filteredCores
                    filterResults.count = filteredCores.size
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(val binding: ListItemCoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(core: Core) {
            binding.root.findNavController().navigate(
                R.id.action_vehicles_page_fragment_to_core_details_fragment,
                bundleOf("core" to core, "title" to core.serial),
                null,
                FragmentNavigatorExtras(binding.listItemCoreCard to core.id)
            )
        }
    }
}