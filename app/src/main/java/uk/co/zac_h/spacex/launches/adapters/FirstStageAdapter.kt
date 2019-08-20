package uk.co.zac_h.spacex.launches.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.data.CoreSpecModel

class FirstStageAdapter(private val cores: List<CoreSpecModel>?) :
    RecyclerView.Adapter<FirstStageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_first_stage, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val core = cores?.get(position)

        holder.apply {
            coreSerial.text = core?.serial

            reusedImage.apply {
                core?.reused?.let { reused ->
                    if (reused) {
                        setSuccessImage(R.drawable.ic_check_circle_black_24dp, R.color.success)
                    } else {
                        setSuccessImage(R.drawable.ic_remove_circle_black_24dp, R.color.failed)
                    }
                }
            }

            landedImage.apply {
                core?.landingSuccess?.let { landingSuccess ->
                    if (landingSuccess) {
                        setSuccessImage(R.drawable.ic_check_circle_black_24dp, R.color.success)
                    } else {
                        setSuccessImage(R.drawable.ic_remove_circle_black_24dp, R.color.failed)
                    }
                }
            }

            landingImage.apply {
                core?.landingIntent?.let { landingIntent ->
                    if (landingIntent) {
                        when (core.landingType) {
                            "ASDS" -> {
                                setSuccessImage(R.drawable.ic_waves, R.color.ocean)
                            }
                            "RTLS" -> {
                                setSuccessImage(R.drawable.ic_landscape, R.color.landscape)
                            }
                        }
                    } else {
                        setSuccessImage(R.drawable.ic_remove_circle_black_24dp, R.color.failed)
                    }
                }
            }
        }
    }

    private fun ImageView.setSuccessImage(resId: Int, colorId: Int) {
        setImageResource(resId)
        imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, colorId))
    }

    override fun getItemCount(): Int = cores?.size ?: 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coreSerial: TextView = itemView.findViewById(R.id.list_item_first_stage_core_serial_text)
        val reusedImage: ImageView = itemView.findViewById(R.id.list_item_first_stage_reused_image)
        val landedImage: ImageView = itemView.findViewById(R.id.list_item_first_stage_landed_image)
        val landingImage: ImageView = itemView.findViewById(R.id.list_item_first_stage_landing_image)
    }
}