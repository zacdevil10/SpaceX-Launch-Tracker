package uk.co.zac_h.spacex.launches.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_launch_details.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.launches.adapters.FirstStageAdapter
import uk.co.zac_h.spacex.launches.adapters.PayloadAdapter
import uk.co.zac_h.spacex.utils.data.LaunchesModel
import uk.co.zac_h.spacex.utils.format

class LaunchDetailsFragment : Fragment() {

    private var coreAssigned: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_launch_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val launch = arguments?.getParcelable("launch") as LaunchesModel?

        var blockText = ""

        launch_details_number_text.text = context?.getString(R.string.flight_number, launch?.flightNumber)

        launch?.rocket?.firstStage?.cores?.forEach { i ->
            if (i.block == null) {
                blockText = "TBD "
                return@forEach
            }
            blockText += "${i.block} "
        }

        launch_details_block_text.text = context?.getString(
            R.string.vehicle_block_type,
            launch?.rocket?.name,
            blockText.dropLast(1).replace(" ", " | ")
        )
        launch_details_mission_name_text.text = launch?.missionName
        launch_details_site_name_text.text = launch?.launchSite?.name
        launch_details_date_text.text = launch?.launchDateUnix?.format()

        launch?.staticFireDateUnix?.let {
            launch_details_static_fire_date_text.text = it.format()
        }

        launch_details_details_text.text = launch?.details

        Picasso.get().load(launch?.links?.missionPatchSmall).into(launch_details_mission_patch_image)

        launch?.rocket?.firstStage?.cores?.forEach {
            if (coreAssigned) return@forEach
            coreAssigned = it.serial != null
        }

        if (coreAssigned) {
            launch_details_cores_recycler.apply {
                layoutManager = LinearLayoutManager(this@LaunchDetailsFragment.context)
                setHasFixedSize(true)
                adapter = FirstStageAdapter(launch?.rocket?.firstStage?.cores)
            }
        }

        launch_details_payload_recycler.apply {
            layoutManager = LinearLayoutManager(this@LaunchDetailsFragment.context)
            setHasFixedSize(true)
            adapter = PayloadAdapter(launch?.rocket?.secondStage?.payloads)
        }
    }

}
