package uk.co.zac_h.spacex.launches.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.launches.details.details.LaunchDetailsContract
import uk.co.zac_h.spacex.utils.models.LinksModel

class LaunchLinksAdapter(
    private val links: ArrayList<LinksModel>,
    private val view: LaunchDetailsContract.LaunchDetailsView
) : RecyclerView.Adapter<LaunchLinksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_launch_links,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val link = links[position]

        holder.apply {
            title.text = link.title

            with(link.title) {
                when {
                    contains("Watch") -> icon.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                    contains("Reddit") -> icon.setImageResource(R.drawable.reddit)
                    contains("Press") -> icon.setImageResource(R.drawable.ic_assignment_black_24dp)
                    contains("Wikipedia") -> icon.setImageResource(R.drawable.wikipedia)
                }
            }

            itemView.setOnClickListener {
                view.openWebLink(link.link)
            }
        }
    }

    override fun getItemCount(): Int = links.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.list_item_icon_image)
        val title: TextView = itemView.findViewById(R.id.list_item_title_text)
    }
}