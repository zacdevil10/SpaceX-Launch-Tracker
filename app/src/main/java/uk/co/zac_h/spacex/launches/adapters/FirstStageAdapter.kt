package uk.co.zac_h.spacex.launches.adapters

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
import uk.co.zac_h.spacex.model.spacex.LaunchCoreExtendedModel
import uk.co.zac_h.spacex.utils.setImageAndTint

class FirstStageAdapter(private val cores: List<LaunchCoreExtendedModel>) :
    RecyclerView.Adapter<FirstStageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_first_stage,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val core = cores[position]

        holder.apply {
            cardView.transitionName = core.core?.id ?: ""

            coreSerial.text = core.core?.serial ?: "Unknown"

            reusedImage.apply {
                core.reused?.let { reused ->
                    if (reused) {
                        setImageAndTint(R.drawable.ic_check_circle_black_24dp, R.color.success)
                    } else {
                        setImageAndTint(R.drawable.ic_remove_circle_black_24dp, R.color.failed)
                    }
                }
            }

            landedImage.apply {
                core.landingSuccess?.let { landingSuccess ->
                    if (landingSuccess) {
                        setImageAndTint(R.drawable.ic_check_circle_black_24dp, R.color.success)
                    } else {
                        setImageAndTint(R.drawable.ic_remove_circle_black_24dp, R.color.failed)
                    }
                }
            }

            landingImage.apply {
                core.landingAttempt?.let { landingIntent ->
                    if (landingIntent) {
                        when (core.landingType) {
                            "ASDS" -> setImageAndTint(R.drawable.ic_waves, R.color.ocean)
                            "RTLS" -> setImageAndTint(R.drawable.ic_landscape, R.color.landscape)
                        }
                    } else {
                        setImageAndTint(R.drawable.ic_remove_circle_black_24dp, R.color.failed)
                    }
                }
            }

            core.core?.let {
                detailsImage.visibility = View.VISIBLE
                cardView.setOnClickListener {
                    bind(core)
                }
            } ?: run {
                detailsImage.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = cores.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: MaterialCardView = itemView.findViewById(R.id.list_item_first_stage_card)
        val detailsImage: ImageView =
            itemView.findViewById(R.id.list_item_first_stage_details_image)
        val coreSerial: TextView =
            itemView.findViewById(R.id.list_item_first_stage_core_serial_text)
        val reusedImage: ImageView = itemView.findViewById(R.id.list_item_first_stage_reused_image)
        val landedImage: ImageView = itemView.findViewById(R.id.list_item_first_stage_landed_image)
        val landingImage: ImageView =
            itemView.findViewById(R.id.list_item_first_stage_landing_image)

        fun bind(core: LaunchCoreExtendedModel) {
            itemView.findNavController()
                .navigate(
                    R.id.action_launch_details_container_fragment_to_core_details_fragment,
                    bundleOf("core" to core.core),
                    null,
                    FragmentNavigatorExtras(cardView to (core.core?.id ?: ""))
                )
        }
    }
}