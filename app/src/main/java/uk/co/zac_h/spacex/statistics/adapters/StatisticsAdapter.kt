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
import uk.co.zac_h.spacex.statistics.StatisticsContract
import uk.co.zac_h.spacex.utils.PadType

class StatisticsAdapter(private val view: StatisticsContract.View) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val titles = listOf(
        "Launch History",
        "Landing History",
        "Launch Rate",
        "Mass to Orbit",
        "Fairing Recovery",
        "Launchpads",
        "Landing Pads"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            1 -> {
                FooterViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_illustrations_footer,
                        parent,
                        false
                    )
                )
            }
            else -> {
                ViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_statistics,
                        parent,
                        false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.apply {
                val heading = titles[position]

                itemView.transitionName = heading
                title.text = heading

                graphics.setImageResource(
                    when (position) {
                        0, 1 -> R.drawable.ic_launch_history
                        2 -> R.drawable.ic_launch_rate
                        3 -> R.drawable.ic_mass_to_orbit
                        4 -> R.drawable.ic_fairing_recovery
                        5 -> R.drawable.ic_launchpads
                        6 -> R.drawable.ic_landing_pads
                        else -> R.drawable.ic_baseline_error_outline_24
                    }
                )

                if (position == 0) graphics.scaleX = -1f

                card.setOnClickListener {
                    itemView.findNavController().navigate(
                        when (position) {
                            0 -> R.id.action_statistics_fragment_to_launch_history
                            1 -> R.id.action_statistics_fragment_to_landing_history
                            2 -> R.id.action_statistics_fragment_to_launch_rate
                            3 -> R.id.action_statistics_fragment_to_launch_mass
                            4 -> R.id.action_statistics_fragment_to_fairing_recovery
                            5 -> R.id.action_statistics_fragment_to_pad_stats
                            6 -> R.id.action_statistics_fragment_to_pad_stats
                            else -> R.id.action_statistics_fragment_to_launch_history
                        },
                        bundleOf(
                            when (position) {
                                5 -> "type" to PadType.LAUNCHPAD
                                6 -> "type" to PadType.LANDING_PAD
                                else -> "" to ""
                            },
                            "heading" to heading
                        ),
                        null,
                        FragmentNavigatorExtras(itemView to heading)
                    )
                }
            }
            is FooterViewHolder -> holder.apply {
                itemView.setOnClickListener {
                    view.openWebLink("https://stories.freepik.com/")
                }
            }
        }
    }

    override fun getItemCount(): Int = titles.size + 1

    override fun getItemViewType(position: Int): Int = if (position == titles.size) 1 else 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var card: MaterialCardView = itemView.findViewById(R.id.list_item_statistics_card)
        var title: TextView = itemView.findViewById(R.id.list_item_statistics_title)
        val graphics: ImageView = itemView.findViewById(R.id.list_item_statistics_graphic)
    }

    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}