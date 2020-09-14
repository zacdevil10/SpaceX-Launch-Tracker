package uk.co.zac_h.spacex.statistics.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.StatsPadModel
import uk.co.zac_h.spacex.utils.setImageAndTint

class PadStatsSitesAdapter(private var sites: ArrayList<StatsPadModel>) :
    RecyclerView.Adapter<PadStatsSitesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_pad_stats,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val site = sites[position]

        holder.apply {
            nameText.text = site.name
            attemptedText.text = site.attempts.toString()
            successText.text = site.successes.toString()
            statusImage.setImageResource(
                when (site.status) {
                    "active" -> R.drawable.ic_check_circle_black_24dp
                    "retired" -> R.drawable.ic_remove_circle_black_24dp
                    "under construction" -> R.drawable.ic_build_black_24dp
                    else -> R.drawable.ic_remove_circle_black_24dp
                }
            )

            when (site.type) {
                "ASDS" -> type.setImageAndTint(R.drawable.ic_waves, R.color.ocean)
                "RTLS" -> type.setImageAndTint(R.drawable.ic_landscape, R.color.landscape)
            }
        }
    }

    override fun getItemCount(): Int = sites.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var type: ImageView = itemView.findViewById(R.id.list_item_pad_type_image)
        var nameText: TextView = itemView.findViewById(R.id.list_item_pad_name_text)
        var attemptedText: TextView = itemView.findViewById(R.id.list_item_pad_attempted_text)
        var successText: TextView = itemView.findViewById(R.id.list_item_pad_success_text)
        var statusImage: ImageView = itemView.findViewById(R.id.list_item_pad_status_image)
    }
}