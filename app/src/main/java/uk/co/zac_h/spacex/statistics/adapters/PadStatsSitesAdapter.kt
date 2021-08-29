package uk.co.zac_h.spacex.statistics.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemPadStatsBinding
import uk.co.zac_h.spacex.dto.spacex.PadStatus
import uk.co.zac_h.spacex.dto.spacex.StatsPadModel
import uk.co.zac_h.spacex.utils.setImageAndTint

class PadStatsSitesAdapter(private var sites: ArrayList<StatsPadModel>) :
    RecyclerView.Adapter<PadStatsSitesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemPadStatsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val site = sites[position]

        holder.binding.apply {
            listItemPadNameText.text = site.name
            listItemPadAttemptedText.text = site.attempts.toString()
            listItemPadSuccessText.text = site.successes.toString()
            listItemPadStatusImage.setImageResource(
                when (site.status) {
                    PadStatus.ACTIVE -> R.drawable.ic_check_circle_black_24dp
                    PadStatus.RETIRED -> R.drawable.ic_remove_circle_black_24dp
                    PadStatus.UNDER_CONSTRUCTION -> R.drawable.ic_build_black_24dp
                    else -> R.drawable.ic_remove_circle_black_24dp
                }
            )

            when (site.type) {
                "ASDS" -> listItemPadTypeImage.setImageAndTint(R.drawable.ic_waves, R.color.ocean)
                "RTLS" -> listItemPadTypeImage.setImageAndTint(
                    R.drawable.ic_landscape,
                    R.color.landscape
                )
            }
        }
    }

    override fun getItemCount(): Int = sites.size

    class ViewHolder(val binding: ListItemPadStatsBinding) : RecyclerView.ViewHolder(binding.root)
}