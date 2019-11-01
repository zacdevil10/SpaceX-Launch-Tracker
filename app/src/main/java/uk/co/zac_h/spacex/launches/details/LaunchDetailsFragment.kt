package uk.co.zac_h.spacex.launches.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.*
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_launch_details.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.launches.adapters.FirstStageAdapter
import uk.co.zac_h.spacex.launches.adapters.LaunchLinksAdapter
import uk.co.zac_h.spacex.launches.adapters.PayloadAdapter
import uk.co.zac_h.spacex.model.LaunchesModel
import uk.co.zac_h.spacex.utils.LinksModel
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper
import uk.co.zac_h.spacex.utils.formatDateMillisLong

class LaunchDetailsFragment : Fragment(),
    LaunchDetailsView {

    private lateinit var presenter: LaunchDetailsPresenter
    private lateinit var pinnedSharedPreferences: PinnedSharedPreferencesHelper

    private var launch: LaunchesModel? = null
    private var id: String? = null

    private var pinned: Boolean = false

    private var coreAssigned: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        launch = arguments?.getParcelable("launch") as LaunchesModel?
        id = arguments?.getString("launch_id")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_launch_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pinnedSharedPreferences = PinnedSharedPreferencesHelper(
            context?.getSharedPreferences(
                "pinned",
                Context.MODE_PRIVATE
            )
        )

        presenter = LaunchDetailsPresenterImpl(
            this,
            LaunchDetailsHelperImpl(pinnedSharedPreferences),
            LaunchDetailsInteractorImpl()
        )

        launch?.let {
            presenter.addLaunchModel(it)
            pinned = presenter.isPinned()
        } ?: id?.let {
            presenter.getLaunch(it)
            pinned = presenter.isPinned(it.toInt())
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
        ).apply {
            duration = 500
        }

        launch_details_first_stage_collapse_toggle.setOnCheckedChangeListener { compoundButton, _ ->
            compoundButton.startAnimation(rotation)
        }

        launch_details_payload_collapse_toggle.setOnCheckedChangeListener { compoundButton, _ ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.cancelRequest()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details, menu)
        menu.findItem(R.id.pin).icon = context?.let {
            ContextCompat.getDrawable(
                it,
                if (id?.let { id -> presenter.isPinned(id.toInt()) }
                        ?: presenter.isPinned()) R.drawable.ic_star_black_24dp else R.drawable.ic_star_border_black_24dp)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.pin -> {
                presenter.pinLaunch(!pinned)

                pinned = !pinned

                item.icon = context?.let {
                    ContextCompat.getDrawable(
                        it,
                        if (pinned) R.drawable.ic_star_black_24dp else R.drawable.ic_star_border_black_24dp
                    )
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun updateLaunchDataView(launch: LaunchesModel?) {
        launch?.let {
            this.launch = launch

            launch_details_mission_patch_image.visibility =
                launch.links.missionPatchSmall?.let { View.VISIBLE } ?: View.GONE

            Picasso.get().load(launch.links.missionPatchSmall)
                .into(launch_details_mission_patch_image)

            launch_details_number_text.text = context?.getString(
                R.string.flight_number,
                launch.flightNumber
            )
            launch_details_rocket_type_text.text = launch.rocket.name
            launch_details_mission_name_text.text = launch.missionName
            launch_details_site_name_text.text = launch.launchSite.name
            launch_details_date_text.text = launch.tbd?.let { tbd ->
                launch.launchDateUnix.formatDateMillisLong(tbd)
            } ?: launch.launchDateUnix.formatDateMillisLong()

            launch.staticFireDateUnix?.let {
                launch_details_static_fire_date_label.visibility = View.VISIBLE
                launch_details_static_fire_date_text.text = it.formatDateMillisLong()
            }

            launch_details_details_text.text = launch.details

            launch_details_calendar_button.setOnClickListener {
                val calendarIntent = Intent(Intent.ACTION_INSERT).apply {
                    data = CalendarContract.Events.CONTENT_URI
                    putExtra(
                        CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        launch.launchDateUnix.times(1000L)
                    )
                    putExtra(
                        CalendarContract.EXTRA_EVENT_END_TIME,
                        launch.launchDateUnix.times(1000L).plus(3600000)
                    )
                    putExtra(
                        CalendarContract.Events.TITLE,
                        "${launch.missionName} - ${launch.rocket.name} Launch Event"
                    )
                }
                startActivity(calendarIntent)
            }

            launch.rocket.firstStage?.cores?.forEach {
                if (coreAssigned) return@forEach
                coreAssigned = it.serial != null
            }

            if (coreAssigned) launch.rocket.firstStage?.cores?.let {
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
                addItemDecoration(
                    DividerItemDecoration(
                        this.context,
                        DividerItemDecoration.VERTICAL
                    )
                )
                adapter = PayloadAdapter(
                    this@LaunchDetailsFragment.context,
                    launch.rocket.secondStage?.payloads
                )
                (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            }

            val links = ArrayList<LinksModel>()

            launch.links.videoLink?.let { links.add(LinksModel("Watch", it)) }
            launch.links.redditCampaign?.let { links.add(LinksModel("Reddit Campaign", it)) }
            launch.links.redditLaunch?.let { links.add(LinksModel("Reddit Launch", it)) }
            launch.links.redditMedia?.let { links.add(LinksModel("Reddit Media", it)) }
            launch.links.presskit?.let { links.add(LinksModel("Press Kit", it)) }
            launch.links.wikipedia?.let { links.add(LinksModel("Wikipedia Article", it)) }

            if (links.isEmpty()) launch_details_links_text.visibility = View.GONE

            launch_details_links_recycler.apply {
                layoutManager = GridLayoutManager(this@LaunchDetailsFragment.context, 2)
                setHasFixedSize(true)
                adapter = LaunchLinksAdapter(links, this@LaunchDetailsFragment)
            }
        }
    }

    /**
     * Toggle expand/collapse of [recycler] views on selection of TextView
     *
     * @param[recycler] is the view you wish to show or hide
     * @param[expCollapse] is the toggle button that shows the collapse state of the recycler
     */
    private fun expandCollapse(recycler: RecyclerView, expCollapse: ToggleButton) {
        when {
            expCollapse.isChecked -> recycler.visibility = View.GONE
            else -> recycler.visibility = View.VISIBLE
        }
        expCollapse.isChecked = !expCollapse.isChecked
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

    override fun openWebLink(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
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
