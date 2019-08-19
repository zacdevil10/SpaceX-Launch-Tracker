package uk.co.zac_h.spacex.launches.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_launch_details.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.data.LaunchesModel
import uk.co.zac_h.spacex.utils.format

class LaunchDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launch_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val launch = arguments?.getParcelable("launch") as LaunchesModel?

        var blockText = ""

        if (launch != null) {
            launch_details_number_text.text = context?.getString(R.string.flight_number, launch.flightNumber)

            launch.rocket.firstStage?.cores?.forEach { i ->
                if (i.block == null) {
                    blockText = "TBD "
                    return@forEach
                }
                blockText += "${i.block} "
            }

            launch_details_block_text.text = context?.getString(
                R.string.vehicle_block_type,
                launch.rocket.name,
                blockText.dropLast(1).replace(" ", " | ")
            )
            launch_details_mission_name_text.text = launch.missionName
            launch_details_site_name_text.text = launch.launchSite.name
            launch_details_date_text.text = launch.launchDateUnix.format()
            if (launch.staticFireDateUnix != null) launch_details_static_fire_date_text.text =
                launch.staticFireDateUnix.format()

            launch_details_details_text.text = launch.details

            Picasso.get().load(launch.links.missionPatchSmall).into(launch_details_mission_patch_image)
        }
    }

}
