package uk.co.zac_h.spacex.statistics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemKeyBinding
import uk.co.zac_h.spacex.utils.metricFormat
import uk.co.zac_h.spacex.utils.models.KeysModel

class StatisticsKeyAdapter(
    private val context: Context,
    private var keys: ArrayList<KeysModel>,
    private val format: Boolean
) :
    RecyclerView.Adapter<StatisticsKeyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemKeyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = keys[position]

        holder.binding.apply {
            listItemKeyLabel.text = key.label
            listItemKeyValue.text = if (format) context.getString(
                R.string.mass_kg,
                key.value.metricFormat()
            ) else key.value.toInt().toString()
        }
    }

    override fun getItemCount(): Int = keys.size

    class ViewHolder(val binding: ListItemKeyBinding) : RecyclerView.ViewHolder(binding.root)

}