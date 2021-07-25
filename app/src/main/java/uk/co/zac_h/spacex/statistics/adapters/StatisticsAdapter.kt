package uk.co.zac_h.spacex.statistics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemIllustrationsFooterBinding
import uk.co.zac_h.spacex.databinding.ListItemStatisticsBinding
import uk.co.zac_h.spacex.statistics.StatisticsContract
import uk.co.zac_h.spacex.utils.PadType

class StatisticsAdapter(private val context: Context, private val view: StatisticsContract.View) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = listOf(
        Statistics.LAUNCH_HISTORY,
        Statistics.LANDING_HISTORY,
        Statistics.LAUNCH_RATE,
        Statistics.MASS_TO_ORBIT,
        Statistics.FAIRING_RECOVERY,
        Statistics.LAUNCHPADS,
        Statistics.LANDING_PADS
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            1 -> FooterViewHolder(
                ListItemIllustrationsFooterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> ViewHolder(
                ListItemStatisticsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.binding.apply {
                val item = items[position]

                val title = context.getString(item.title)

                listItemStatisticsCard.transitionName = title
                listItemStatisticsTitle.text = title

                listItemStatisticsGraphic.setImageResource(item.image)

                if (position == 0) listItemStatisticsGraphic.scaleX = -1f

                listItemStatisticsCard.setOnClickListener {
                    root.findNavController().navigate(
                        item.nav,
                        bundleOf(
                            when (item) {
                                Statistics.LAUNCHPADS -> "type" to PadType.LAUNCHPAD
                                Statistics.LANDING_PADS -> "type" to PadType.LANDING_PAD
                                else -> "" to ""
                            },
                            "heading" to title
                        ),
                        null,
                        FragmentNavigatorExtras(listItemStatisticsCard to title)
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

    override fun getItemCount(): Int = items.size + 1

    override fun getItemViewType(position: Int): Int = if (position == items.size) 1 else 0

    class ViewHolder(val binding: ListItemStatisticsBinding) : RecyclerView.ViewHolder(binding.root)

    class FooterViewHolder(val binding: ListItemIllustrationsFooterBinding) :
        RecyclerView.ViewHolder(binding.root)
}

enum class Statistics(@StringRes val title: Int, val nav: Int, val image: Int) {
    LAUNCH_HISTORY(
        R.string.statistics_launch_history,
        R.id.action_statistics_fragment_to_launch_history,
        R.drawable.ic_launch_history
    ),
    LANDING_HISTORY(
        R.string.statistics_landing_history,
        R.id.action_statistics_fragment_to_landing_history,
        R.drawable.ic_launch_history
    ),
    LAUNCH_RATE(
        R.string.statistics_launch_rate,
        R.id.action_statistics_fragment_to_launch_rate,
        R.drawable.ic_launch_rate
    ),
    MASS_TO_ORBIT(
        R.string.statistics_mass_to_orbit,
        R.id.action_statistics_fragment_to_launch_mass,
        R.drawable.ic_mass_to_orbit
    ),
    FAIRING_RECOVERY(
        R.string.statistics_fairing_recovery,
        R.id.action_statistics_fragment_to_fairing_recovery,
        R.drawable.ic_fairing_recovery
    ),
    LAUNCHPADS(
        R.string.statistics_launchpads,
        R.id.action_statistics_fragment_to_pad_stats,
        R.drawable.ic_launchpads
    ),
    LANDING_PADS(
        R.string.statistics_landing_pads,
        R.id.action_statistics_fragment_to_pad_stats,
        R.drawable.ic_landing_pads
    )
}