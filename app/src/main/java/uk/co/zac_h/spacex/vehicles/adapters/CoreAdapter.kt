package uk.co.zac_h.spacex.vehicles.adapters

import android.annotation.SuppressLint
import android.content.Context
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
import uk.co.zac_h.spacex.model.spacex.CoreModel
import uk.co.zac_h.spacex.utils.formatDateMillisShort
import java.util.*
import kotlin.collections.ArrayList

class CoreAdapter(private val context: Context?, private val cores: ArrayList<CoreModel>) :
    RecyclerView.Adapter<CoreAdapter.ViewHolder>(), Filterable {

    private var filteredCores: ArrayList<CoreModel>

    init {
        filteredCores = cores
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_core,
                parent,
                false
            )
        )

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val core = filteredCores[position]

        holder.apply {
            itemView.transitionName = core.serial

            serial.text = core.serial
            block.text = core.block?.let { context?.getString(R.string.block, it) } ?: ""

            core.block?.let {
                block.text = context?.getString(R.string.block, it)
                separator.visibility = View.VISIBLE
            } ?: run {
                block.text = ""
                separator.visibility = View.GONE
            }

            details.text = core.details
            status.text = core.status?.capitalize() ?: "Unknown"
            flights.text = core.reuseCount.toString()
            date.text = core.originalLaunchDateUnix?.formatDateMillisShort() ?: "TBD"

            button.setOnClickListener { bind(core) }
            card.setOnClickListener { bind(core) }
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
                            val filteredList = ArrayList<CoreModel>()
                            cores.forEach { core ->
                                if (core.serial.toLowerCase(Locale.getDefault()).contains(
                                        search.toString().toLowerCase(
                                            Locale.getDefault()
                                        )
                                    )
                                ) {
                                    filteredList.add(core)
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.list_item_core_card)
        val serial: TextView = itemView.findViewById(R.id.list_item_core_serial)
        val separator: View = itemView.findViewById(R.id.list_item_core_title_separator)
        val block: TextView = itemView.findViewById(R.id.list_item_core_block_text)
        val details: TextView = itemView.findViewById(R.id.list_item_core_details)
        val status: TextView = itemView.findViewById(R.id.list_item_core_status_text)
        val flights: TextView = itemView.findViewById(R.id.list_item_core_flights_text)
        val date: TextView = itemView.findViewById(R.id.list_item_core_date_text)
        val button: Button = itemView.findViewById(R.id.list_item_core_specs_button)

        fun bind(core: CoreModel) {
            itemView.findNavController().navigate(
                R.id.action_vehicles_page_fragment_to_core_details_fragment,
                bundleOf("core" to core, "title" to core.serial),
                null,
                FragmentNavigatorExtras(itemView to core.serial)
            )
        }
    }
}