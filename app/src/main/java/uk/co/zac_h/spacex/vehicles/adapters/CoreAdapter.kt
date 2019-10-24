package uk.co.zac_h.spacex.vehicles.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.CoreModel
import uk.co.zac_h.spacex.utils.formatDateMillisShort

class CoreAdapter(private val context: Context?, private val cores: ArrayList<CoreModel>) :
    RecyclerView.Adapter<CoreAdapter.ViewHolder>() {

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
        val core = cores[position]

        holder.apply {
            serial.text = core.serial
            core.block?.let { block.text = context?.getString(R.string.block, it) }
            details.text = core.details
            status.text = core.status?.capitalize() ?: "Unknown"
            date.text = core.originalLaunchDateUnix?.formatDateMillisShort() ?: "TBD"

            button.setOnClickListener { bind(core) }
            card.setOnClickListener { bind(core) }
        }
    }

    override fun getItemCount(): Int = cores.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.list_item_core_card)
        val serial: TextView = itemView.findViewById(R.id.list_item_core_serial)
        val block: TextView = itemView.findViewById(R.id.list_item_core_block_text)
        val details: TextView = itemView.findViewById(R.id.list_item_core_details)
        val status: TextView = itemView.findViewById(R.id.list_item_core_status_text)
        val date: TextView = itemView.findViewById(R.id.list_item_core_date_text)
        val button: Button = itemView.findViewById(R.id.list_item_core_specs_button)

        fun bind(core: CoreModel) {
            itemView.findNavController().navigate(
                R.id.action_vehicles_page_fragment_to_core_details_fragment,
                bundleOf("core" to core, "title" to core.serial)
            )
        }
    }
}