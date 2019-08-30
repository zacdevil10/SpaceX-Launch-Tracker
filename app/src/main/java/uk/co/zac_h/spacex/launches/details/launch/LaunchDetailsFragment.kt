package uk.co.zac_h.spacex.launches.details.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_launch_details.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.launches.adapters.FirstStageAdapter
import uk.co.zac_h.spacex.launches.adapters.PayloadAdapter
import uk.co.zac_h.spacex.utils.data.LaunchesModel
import uk.co.zac_h.spacex.utils.format
import uk.co.zac_h.spacex.utils.formatBlockNumber

class LaunchDetailsFragment : Fragment(),
    LaunchDetailsView {

    private lateinit var presenter: LaunchDetailsPresenter

    private var coreAssigned: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_launch_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchDetailsPresenterImpl(
            this,
            LaunchDetailsInteractorImpl()
        )

        val launch = arguments?.getParcelable("launch") as LaunchesModel?
        val id = arguments?.getString("launch_id")

        launch?.let {
            presenter.addLaunchModel(it)
        } ?: id?.let {
            presenter.getLaunch(it)
        }

        launch_details_first_stage_text.setOnClickListener {
            expandCollapse(
                launch_details_cores_recycler,
                launch_details_first_stage_collapse_toggle
            )
        }

        launch_details_payload_text.setOnClickListener {
            expandCollapse(
                launch_details_payload_recycler,
                launch_details_payload_collapse_toggle
            )
        }

        //Set rotation animation of toggle buttons when expanding/collapsing recycler view
        val rotation = RotateAnimation(
            180f,
            0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotation.duration = 500

        launch_details_first_stage_collapse_toggle.setOnCheckedChangeListener { compoundButton, b ->
            compoundButton.startAnimation(rotation)
        }

        launch_details_payload_collapse_toggle.setOnCheckedChangeListener { compoundButton, b ->
            compoundButton.startAnimation(rotation)
        }
    }

    override fun onResume() {
        super.onResume()

        /**
         * Set and restore Expand/Collapse state of recycler view when returning to fragment
         */
        setupExpandCollapse(
            launch_details_cores_recycler,
            launch_details_first_stage_collapse_toggle
        )
        setupExpandCollapse(launch_details_payload_recycler, launch_details_payload_collapse_toggle)
    }

    override fun updateLaunchDataView(launch: LaunchesModel?) {
        launch?.let {
            Picasso.get().load(launch.links.missionPatchSmall)
                .into(launch_details_mission_patch_image)

            launch_details_number_text.text = context?.getString(
                R.string.flight_number,
                launch.flightNumber
            )
            launch_details_block_text.text = context?.getString(
                R.string.vehicle_block_type,
                launch.rocket.name,
                launch.rocket.firstStage?.cores?.formatBlockNumber()
            )
            launch_details_mission_name_text.text = launch.missionName
            launch_details_site_name_text.text = launch.launchSite.name
            launch_details_date_text.text = launch.tbd?.let { tbd ->
                launch.launchDateUnix.format(tbd)
            } ?: launch.launchDateUnix.format()

            launch.staticFireDateUnix?.let {
                launch_details_static_fire_date_text.text = it.format()
            }

            launch_details_details_text.text = launch.details

            launch.rocket.firstStage?.cores?.forEach {
                if (coreAssigned) return@forEach
                coreAssigned = it.serial != null
            }
        }

        if (coreAssigned) launch?.rocket?.firstStage?.cores?.let {
            //If a core has been assigned to a launch then add the adapter to the RecyclerView
            launch_details_cores_recycler.apply {
                layoutManager = LinearLayoutManager(this@LaunchDetailsFragment.context)
                setHasFixedSize(true)
                adapter = FirstStageAdapter(it)
            }
        } else {
            //If no core has been assigned yet then hide the RecyclerView and related heading
            launch_details_first_stage_text.visibility = View.GONE
            launch_details_first_stage_collapse_toggle.visibility = View.GONE
        }

        launch_details_payload_recycler.apply {
            layoutManager = LinearLayoutManager(this@LaunchDetailsFragment.context)
            setHasFixedSize(false)
            adapter = PayloadAdapter(
                this@LaunchDetailsFragment.context,
                launch?.rocket?.secondStage?.payloads
            )
        }
    }

    /**
     * Toggle expand/collapse of [recycler] views on selection of TextView
     *
     * @param[recycler] is the view you wish to show or hide
     * @param[expCollapse] is the toggle button that shows the collapse state of the recycler
     */
    private fun expandCollapse(recycler: RecyclerView, expCollapse: ToggleButton) {
        if (expCollapse.isChecked) {
            expCollapse.isChecked = false
            recycler.visibility = View.GONE
        } else {
            expCollapse.isChecked = true
            recycler.visibility = View.VISIBLE
        }
    }

    /**
     * Initial expand/collapse of [recycler] views
     *
     * @param[recycler] is the view you wish to show or hide
     * @param[expCollapse] is the toggle button that shows the collapse state of the recycler
     */
    private fun setupExpandCollapse(recycler: RecyclerView, expCollapse: ToggleButton) {
        if (expCollapse.isChecked) {
            expCollapse.isChecked = true
            recycler.visibility = View.VISIBLE
        } else {
            expCollapse.isChecked = false
            recycler.visibility = View.GONE
        }
    }

    override fun showProgress() {
        launch_details_progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        launch_details_progress.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
