package uk.co.zac_h.spacex.statistics.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemIllustrationsFooterBinding
import uk.co.zac_h.spacex.databinding.ListItemStatisticsBinding
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
                    ListItemIllustrationsFooterBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                ViewHolder(
                    ListItemStatisticsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.binding.apply {
                val heading = titles[position]

                listItemStatisticsCard.transitionName = heading
                listItemStatisticsTitle.text = heading

                listItemStatisticsGraphic.setImageResource(
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

                if (position == 0) listItemStatisticsGraphic.scaleX = -1f

                listItemStatisticsCard.setOnClickListener {
                    root.findNavController().navigate(
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
                        FragmentNavigatorExtras(listItemStatisticsCard to heading)
                    )
                }
            }
            is FooterViewHolder -> holder.binding.apply {
                root.setOnClickListener {
                    view.openWebLink("https://stories.freepik.com/")
                }
            }
        }
    }

    override fun getItemCount(): Int = titles.size + 1

    override fun getItemViewType(position: Int): Int = if (position == titles.size) 1 else 0

    class ViewHolder(val binding: ListItemStatisticsBinding) : RecyclerView.ViewHolder(binding.root)

    class FooterViewHolder(val binding: ListItemIllustrationsFooterBinding) :
        RecyclerView.ViewHolder(binding.root)
}