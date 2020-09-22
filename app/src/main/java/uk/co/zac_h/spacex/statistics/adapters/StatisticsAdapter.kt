package uk.co.zac_h.spacex.statistics.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.PadType

class StatisticsAdapter :
    RecyclerView.Adapter<StatisticsAdapter.ViewHolder>() {

    private val titles = listOf(
        "Launch History",
        "Launch Rate",
        "Mass to Orbit",
        "Launchpads",
        "Landing Pads"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_statistics,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val heading = titles[position]

        holder.apply {
            itemView.transitionName = heading
            title.text = heading

            graphics.setImageResource(
                when (position) {
                    0 -> R.drawable.ic_version_control_amico
                    1 -> R.drawable.ic_browser_stats
                    2 -> R.drawable.ic_world
                    3 -> R.drawable.ic_launch_stats
                    4 -> R.drawable.ic_astronaut_bro
                    else -> R.drawable.ic_baseline_error_outline_24
                }
            )

            if (position == 0) graphics.scaleX = -1f

            card.setOnClickListener {
                itemView.findNavController().navigate(
                    when (position) {
                        0 -> R.id.action_statistics_fragment_to_launch_history
                        1 -> R.id.action_statistics_fragment_to_launch_rate
                        2 -> R.id.action_statistics_fragment_to_launch_mass
                        3 -> R.id.action_statistics_fragment_to_pad_stats
                        4 -> R.id.action_statistics_fragment_to_pad_stats
                        else -> R.id.action_statistics_fragment_to_launch_history
                    },
                    bundleOf(
                        when (position) {
                            3 -> "type" to PadType.LAUNCHPAD
                            4 -> "type" to PadType.LANDING_PAD
                            else -> "" to ""
                        },
                        "heading" to heading
                    ),
                    null,
                    FragmentNavigatorExtras(itemView to heading)
                )
            }
        }
    }

    override fun getItemCount(): Int = titles.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var card: MaterialCardView = itemView.findViewById(R.id.list_item_statistics_card)
        var title: TextView = itemView.findViewById(R.id.list_item_statistics_title)
        val graphics: ImageView = itemView.findViewById(R.id.list_item_statistics_graphic)
    }
}