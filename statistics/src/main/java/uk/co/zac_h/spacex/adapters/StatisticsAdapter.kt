package uk.co.zac_h.spacex.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.StatisticsFragmentDirections
import uk.co.zac_h.spacex.statistics.databinding.ListItemIllustrationsFooterBinding
import uk.co.zac_h.spacex.statistics.databinding.ListItemStatisticsBinding
import uk.co.zac_h.spacex.utils.PadType
import uk.co.zac_h.spacex.utils.Statistics

class StatisticsAdapter(private val context: Context, private val openWebLink: (String) -> Unit) :
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
                        getNavDirection(item),
                        FragmentNavigatorExtras(listItemStatisticsCard to title)
                    )
                }
            }
            is FooterViewHolder -> holder.binding.apply {
                root.setOnClickListener {
                    openWebLink("https://stories.freepik.com/")
                }
            }
        }
    }

    private fun getNavDirection(item: Statistics) = when (item) {
        Statistics.LAUNCH_HISTORY ->
            StatisticsFragmentDirections.actionStatisticsToLaunchHistory(Statistics.LAUNCH_HISTORY)
        Statistics.LANDING_HISTORY ->
            StatisticsFragmentDirections.actionStatisticsToLandingHistory(Statistics.LANDING_HISTORY)
        Statistics.LAUNCH_RATE ->
            StatisticsFragmentDirections.actionStatisticsToLaunchRate(Statistics.LAUNCH_RATE)
        Statistics.MASS_TO_ORBIT ->
            StatisticsFragmentDirections.actionStatisticsToLaunchMass(Statistics.MASS_TO_ORBIT)
        Statistics.FAIRING_RECOVERY ->
            StatisticsFragmentDirections.actionStatisticsToFairingRecovery(Statistics.FAIRING_RECOVERY)
        Statistics.LAUNCHPADS ->
            StatisticsFragmentDirections.actionStatisticsToPadStats(Statistics.LAUNCHPADS, PadType.LAUNCHPAD)
        Statistics.LANDING_PADS ->
            StatisticsFragmentDirections.actionStatisticsToPadStats(Statistics.LANDING_PADS, PadType.LANDING_PAD)
    }

    override fun getItemCount(): Int = items.size + 1

    override fun getItemViewType(position: Int): Int = if (position == items.size) 1 else 0

    class ViewHolder(val binding: ListItemStatisticsBinding) : RecyclerView.ViewHolder(binding.root)

    class FooterViewHolder(val binding: ListItemIllustrationsFooterBinding) :
        RecyclerView.ViewHolder(binding.root)
}