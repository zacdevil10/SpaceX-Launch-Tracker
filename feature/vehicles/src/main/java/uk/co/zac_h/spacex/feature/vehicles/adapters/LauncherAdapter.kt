package uk.co.zac_h.spacex.feature.vehicles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.feature.vehicles.VehiclesFragmentDirections
import uk.co.zac_h.spacex.feature.vehicles.databinding.ListItemVehicleBinding
import uk.co.zac_h.spacex.feature.vehicles.launcher.LauncherItem

class LauncherAdapter(private val onClick: (LauncherItem) -> Unit) :
    PagingDataAdapter<LauncherItem, LauncherAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemVehicleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launcher = getItem(position)

        with(holder.binding) {
            vehicleView.apply {
                launcher?.let { image = it.imageUrl }
                title = launcher?.serial
                vehicleSpecs.setOnClickListener {
                    launcher?.let { onClick(it) }
                    holder.bind()
                }
                setOnClickListener {
                    launcher?.let { onClick(it) }
                    holder.bind()
                }
            }
            vehicleDetails.text = launcher?.details
        }
    }

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.findNavController().navigate(
                VehiclesFragmentDirections.actionVehiclesToLauncherDetails()
            )
        }
    }

    object Comparator : DiffUtil.ItemCallback<LauncherItem>() {
        override fun areItemsTheSame(
            oldItem: LauncherItem,
            newItem: LauncherItem
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: LauncherItem,
            newItem: LauncherItem
        ): Boolean = oldItem == newItem
    }
}