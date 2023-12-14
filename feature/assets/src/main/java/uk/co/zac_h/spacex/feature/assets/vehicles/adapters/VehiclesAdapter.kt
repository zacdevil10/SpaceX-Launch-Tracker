package uk.co.zac_h.spacex.feature.assets.vehicles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.core.ui.VehicleView
import uk.co.zac_h.spacex.feature.assets.R
import uk.co.zac_h.spacex.feature.assets.databinding.ListItemVehicleBinding
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehiclesFragmentDirections

class VehiclesAdapter(val setSelected: (VehicleItem) -> Unit) :
    ListAdapter<VehicleItem, VehiclesAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemVehicleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vehicle = getItem(position)

        holder.binding.vehicleView.setContent {
            SpaceXTheme {
                VehicleView(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    image = vehicle.imageUrl,
                    title = vehicle.title,
                    buttonText = stringResource(R.string.vehicle_list_item_specs_button_label),
                    navigate = {
                        setSelected(vehicle)
                        holder.bind()
                    }
                ) {
                    vehicle.description?.let {
                        Text(
                            text = it,
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 24.dp)
                        )
                    }
                }
            }
        }
    }

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.findNavController().navigate(
                VehiclesFragmentDirections.actionVehiclesPageToVehicleDetails()
            )
        }
    }

    companion object Comparator : DiffUtil.ItemCallback<VehicleItem>() {

        override fun areItemsTheSame(oldItem: VehicleItem, newItem: VehicleItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: VehicleItem, newItem: VehicleItem) =
            oldItem.title == newItem.title
                    && oldItem.imageUrl == newItem.imageUrl
                    && oldItem.description == newItem.description
                    && oldItem.longDescription == newItem.longDescription
    }
}