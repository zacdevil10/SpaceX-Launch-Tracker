package uk.co.zac_h.spacex.statistics.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.data.StatsPadModel
import uk.co.zac_h.spacex.utils.setImageAndTint

class PadStatsSitesAdapter(private var sites: ArrayList<StatsPadModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> {
                HeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_heading,
                        parent,
                        false
                    )
                )
            }
            1 -> {
                ViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_pad_stats,
                        parent,
                        false
                    )
                )
            }
            else -> null
        }!!

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val site = sites[position]

        when (holder) {
            is ViewHolder -> holder.apply {
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

                if (site.type == "ASDS") type.apply {
                    setImageAndTint(R.drawable.ic_waves, R.color.ocean)
                }
            }
            is HeaderViewHolder -> holder.apply {
                heading.text = site.name
            }
        }
    }

    override fun getItemCount(): Int = sites.size

    override fun getItemViewType(position: Int): Int = if (sites[position].isHeading) 0 else 1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var type: ImageView = itemView.findViewById(R.id.list_item_pad_type_image)
        var nameText: TextView = itemView.findViewById(R.id.list_item_pad_name_text)
        var attemptedText: TextView = itemView.findViewById(R.id.list_item_pad_attempted_text)
        var successText: TextView = itemView.findViewById(R.id.list_item_pad_success_text)
        var statusImage: ImageView = itemView.findViewById(R.id.list_item_pad_status_image)
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var heading: TextView = itemView.findViewById(R.id.list_item_heading)
    }
}