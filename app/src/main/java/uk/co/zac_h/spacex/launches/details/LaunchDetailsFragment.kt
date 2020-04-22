package uk.co.zac_h.spacex.launches.details

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.view.*
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.google.android.material.transition.MaterialContainerTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_launch_details.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.launches.adapters.FirstStageAdapter
import uk.co.zac_h.spacex.launches.adapters.LaunchLinksAdapter
import uk.co.zac_h.spacex.launches.adapters.PayloadAdapter
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelperImpl
import uk.co.zac_h.spacex.utils.formatDateMillisLong
import uk.co.zac_h.spacex.utils.models.LinksModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchDetailsFragment : Fragment(), LaunchDetailsContract.LaunchDetailsView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var presenter: LaunchDetailsContract.LaunchDetailsPresenter? = null
    private lateinit var pinnedSharedPreferences: PinnedSharedPreferencesHelper

    private var launch: LaunchesModel? = null
    private var id: String? = null

    private var pinned: Boolean = false

    private var coreAssigned: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        postponeEnterTransition()

        sharedElementEnterTransition = MaterialContainerTransform(requireContext())

        launch = if (savedInstanceState != null) {
            savedInstanceState.getParcelable("launch")
        } else {
            arguments?.getParcelable("launch") as LaunchesModel?
        }
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

        pinnedSharedPreferences = PinnedSharedPreferencesHelperImpl(
            context?.getSharedPreferences(
                "pinned",
                Context.MODE_PRIVATE
            )
        )

        presenter = LaunchDetailsPresenterImpl(
            this,
            pinnedSharedPreferences,
            LaunchDetailsInteractorImpl()
        )

        launch?.let {
            presenter?.addLaunchModel(it)
            launch_details_scroll_view.transitionName = it.flightNumber.toString()
            pinned = presenter?.isPinned(it.flightNumber.toString()) ?: false
        } ?: id?.let {
            presenter?.getLaunch(it)
            pinned = presenter?.isPinned(it) ?: false
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

        startPostponedEnterTransition()
    }

    override fun onResume() {
        super.onResume()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
        /**
         * Set and restore Expand/Collapse state of recycler view when returning to fragment
         */
        setupExpandCollapse(
            launch_details_cores_recycler,
            launch_details_first_stage_collapse_toggle
        )
        setupExpandCollapse(launch_details_payload_recycler, launch_details_payload_collapse_toggle)
    }

    override fun onPause() {
        super.onPause()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("launch", launch)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details, menu)
        menu.findItem(R.id.pin).icon = context?.let {
            ContextCompat.getDrawable(
                it,
                if (id?.let { id -> presenter?.isPinned(id) } ?: presenter?.isPinned(launch?.flightNumber.toString()) == true) {
                    R.drawable.ic_star_black_24dp
                } else {
                    R.drawable.ic_star_border_black_24dp
                })
        }

        setIconTint(menu.findItem(R.id.pin))
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.pin -> {
            presenter?.pinLaunch(launch?.flightNumber?.toString() ?: id.toString(), !pinned)

            pinned = !pinned

            item.icon = context?.let {
                ContextCompat.getDrawable(
                    it,
                    if (pinned) R.drawable.ic_star_black_24dp else R.drawable.ic_star_border_black_24dp
                )
            }

            setIconTint(item)
            true
        }
        R.id.create_event -> {
            presenter?.createEvent()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setIconTint(item: MenuItem) {
        var drawable = item.icon
        drawable = DrawableCompat.wrap(drawable)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DrawableCompat.setTint(
                drawable.mutate(),
                resources.getColor(android.R.color.white, null)
            )
        } else {
            @Suppress("DEPRECATION")
            DrawableCompat.setTint(drawable.mutate(), resources.getColor(android.R.color.white))
        }
        item.icon = drawable
    }

    override fun updateLaunchDataView(launch: LaunchesModel?) {
        launch?.let {
            this.launch = launch

            launch_details_scroll_view.transitionName = it.flightNumber.toString()

            if (id == null) id = launch.flightNumber.toString()

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

            launch.staticFireDateUnix?.let { date ->
                launch_details_static_fire_date_label.visibility = View.VISIBLE
                launch_details_static_fire_date_text.text = date.formatDateMillisLong()
            } ?: run {
                launch_details_static_fire_date_text.visibility = View.GONE
            }

            launch_details_details_text.visibility =
                if (launch.details.isNullOrEmpty()) View.GONE else View.VISIBLE
            launch_details_details_text.text = launch.details

            launch.rocket.firstStage?.cores?.forEach { core ->
                if (coreAssigned) return@forEach
                coreAssigned = core.serial != null
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

            launch.links.videoLink?.let { link ->
                links.add(
                    LinksModel(
                        "Watch",
                        link
                    )
                )
            }
            launch.links.redditCampaign?.let { link ->
                links.add(
                    LinksModel(
                        "Reddit Campaign",
                        link
                    )
                )
            }
            launch.links.redditLaunch?.let { link ->
                links.add(
                    LinksModel(
                        "Reddit Launch",
                        link
                    )
                )
            }
            launch.links.redditMedia?.let { link ->
                links.add(
                    LinksModel(
                        "Reddit Media",
                        link
                    )
                )
            }
            launch.links.presskit?.let { link ->
                links.add(
                    LinksModel(
                        "Press Kit",
                        link
                    )
                )
            }
            launch.links.wikipedia?.let { link ->
                links.add(
                    LinksModel(
                        "Wikipedia Article",
                        link
                    )
                )
            }

            if (links.isEmpty()) launch_details_links_text.visibility = View.GONE

            launch_details_links_recycler.apply {
                layoutManager = GridLayoutManager(this@LaunchDetailsFragment.context, 3)
                setHasFixedSize(true)
                adapter = LaunchLinksAdapter(links, this@LaunchDetailsFragment)
            }
        }
    }

    override fun newCalendarEvent() {
        launch?.let {
            val calendarIntent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(
                    CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                    it.launchDateUnix.times(1000L)
                )
                putExtra(
                    CalendarContract.EXTRA_EVENT_END_TIME,
                    it.launchDateUnix.times(1000L).plus(3600000)
                )
                putExtra(
                    CalendarContract.Events.TITLE,
                    "${it.missionName} - SpaceX"
                )
            }
            try {
                startActivity(calendarIntent)
            } catch (e: ActivityNotFoundException) {
                showError("No supported calendar apps found.")
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

    override fun networkAvailable() {
        activity?.runOnUiThread {
            id?.let {
                if (launch == null) presenter?.getLaunch(it)
            }
        }
    }
}
