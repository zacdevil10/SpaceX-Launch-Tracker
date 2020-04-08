package uk.co.zac_h.spacex.dashboard

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.list_item_latest_launch.*
import kotlinx.android.synthetic.main.list_item_next_launch.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.dashboard.adapters.DashboardPinnedAdapter
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.utils.DashboardObj.PREFERENCES_LATEST_LAUNCH
import uk.co.zac_h.spacex.utils.DashboardObj.PREFERENCES_LATEST_NEWS
import uk.co.zac_h.spacex.utils.DashboardObj.PREFERENCES_NEXT_LAUNCH
import uk.co.zac_h.spacex.utils.DashboardObj.PREFERENCES_PINNED_LAUNCH
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelperImpl
import uk.co.zac_h.spacex.utils.formatBlockNumber
import uk.co.zac_h.spacex.utils.formatDateMillisLong
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class DashboardFragment : Fragment(), DashboardContract.DashboardView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var presenter: DashboardContract.DashboardPresenter? = null
    private lateinit var pinnedSharedPreferences: PinnedSharedPreferencesHelper

    private var nextLaunchModel: LaunchesModel? = null
    private var latestLaunchModel: LaunchesModel? = null

    private lateinit var pinnedAdapter: DashboardPinnedAdapter

    private lateinit var pinnedArray: ArrayList<LaunchesModel>
    private lateinit var pinnedKeysArray: ArrayList<String>

    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        savedInstanceState?.let {
            nextLaunchModel = it.getParcelable("next")
            latestLaunchModel = it.getParcelable("latest")
        }

        pinnedArray = savedInstanceState?.let {
            it.getParcelableArrayList<LaunchesModel>("pinned") as ArrayList<LaunchesModel>
        } ?: ArrayList()

        pinnedKeysArray = savedInstanceState?.getStringArrayList("pinned_keys") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        val pinnedPrefs = (context?.applicationContext as App).pinnedPreferencesRepo

        pinnedSharedPreferences = PinnedSharedPreferencesHelperImpl(
            context?.getSharedPreferences("pinned", Context.MODE_PRIVATE)
        )

        presenter = DashboardPresenterImpl(this, DashboardInteractorImpl())

        pinnedAdapter = DashboardPinnedAdapter(context, pinnedArray)

        dashboard_pinned_launches_recycler.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            adapter = pinnedAdapter
        }

        val prefs = (context?.applicationContext as App).dashboardPreferencesRepo

        prefs.visibleLive.observe(viewLifecycleOwner, Observer { mode ->
            mode?.let {
                it.forEach { elements ->
                    when (elements.key) {
                        PREFERENCES_NEXT_LAUNCH ->
                            presenter?.toggleNextLaunchVisibility(elements.value as Boolean)
                        PREFERENCES_LATEST_LAUNCH ->
                            presenter?.toggleLatestLaunchVisibility(elements.value as Boolean)
                        PREFERENCES_PINNED_LAUNCH ->
                            presenter?.togglePinnedList(elements.value as Boolean)
                        PREFERENCES_LATEST_NEWS -> {
                        }
                    }
                }
            }
        })

        pinnedPrefs.pinnedLive.observe(viewLifecycleOwner, Observer { mode ->
            mode?.let {
                it.forEach { element ->
                    if (element.value as Boolean) {
                        if (!pinnedKeysArray.contains(element.key)) presenter?.getSingleLaunch(
                            element.key
                        )
                    } else {
                        pinnedArray.removeAt(pinnedKeysArray.indexOf(element.key))
                        pinnedAdapter.notifyItemRemoved(pinnedKeysArray.indexOf(element.key))
                        pinnedKeysArray.remove(element.key)
                        pinnedSharedPreferences.removePinnedLaunch(element.key)
                    }
                }
            }
        })

        dashboard_swipe_refresh.setOnRefreshListener {
            presenter?.getLatestLaunches()
        }

        presenter?.getLatestLaunches(nextLaunchModel, latestLaunchModel)
    }

    override fun onStart() {
        super.onStart()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onResume() {
        super.onResume()
        dashboard_swipe_refresh.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        dashboard_swipe_refresh.isEnabled = false
    }

    override fun onStop() {
        super.onStop()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        nextLaunchModel?.let { outState.putParcelable("next", it) }
        latestLaunchModel?.let { outState.putParcelable("latest", it) }
        outState.putParcelableArrayList("pinned", pinnedArray)
        outState.putStringArrayList("pinned_keys", pinnedKeysArray)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
        countdownTimer = null
        presenter?.cancelRequests()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.edit -> {
            findNavController().navigate(R.id.action_dashboard_page_fragment_to_dashboard_edit_dialog)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun updateNextLaunch(nextLaunch: LaunchesModel) {
        nextLaunchModel = nextLaunch

        dashboard_next_card_view.visibility = View.VISIBLE

        dashboard_next_card_view.transitionName = nextLaunch.flightNumber.toString()

        dashboard_next_mission_patch_image.visibility =
            nextLaunch.links.missionPatchSmall?.let { View.VISIBLE } ?: View.GONE

        Picasso.get().load(nextLaunch.links.missionPatchSmall)
            .into(dashboard_next_mission_patch_image)

        dashboard_next_flight_no_text.text =
            context?.getString(R.string.flight_number, nextLaunch.flightNumber)

        dashboard_next_block_text.text = context?.getString(
            R.string.vehicle_block_type,
            nextLaunch.rocket.name,
            nextLaunch.rocket.firstStage?.cores?.formatBlockNumber()
        )
        dashboard_next_mission_name_text.text = nextLaunch.missionName
        dashboard_next_date_text.text = nextLaunch.tbd?.let { tbd ->
            nextLaunch.launchDateUnix.formatDateMillisLong(tbd)
        } ?: nextLaunch.launchDateUnix.formatDateMillisLong()

        dashboard_next_card_view.setOnClickListener { _ ->
            findNavController().navigate(
                R.id.action_dashboard_page_fragment_to_launch_details_fragment,
                bundleOf("launch" to nextLaunch, "title" to nextLaunch.missionName),
                null,
                FragmentNavigatorExtras(dashboard_next_card_view to nextLaunch.flightNumber.toString())
            )
        }
    }

    override fun updateLatestLaunch(latestLaunch: LaunchesModel) {
        latestLaunchModel = latestLaunch

        dashboard_latest_card_view.visibility = View.VISIBLE

        dashboard_latest_card_view.transitionName = latestLaunch.flightNumber.toString()

        dashboard_latest_mission_patch_image.visibility =
            latestLaunch.links.missionPatchSmall?.let { View.VISIBLE } ?: View.GONE

        Picasso.get().load(latestLaunch.links.missionPatchSmall)
            .into(dashboard_latest_mission_patch_image)

        dashboard_latest_flight_no_text.text =
            context?.getString(R.string.flight_number, latestLaunch.flightNumber)

        dashboard_latest_block_text.text = context?.getString(
            R.string.vehicle_block_type,
            latestLaunch.rocket.name,
            latestLaunch.rocket.firstStage?.cores?.formatBlockNumber()
        )
        dashboard_latest_mission_name_text.text = latestLaunch.missionName
        dashboard_latest_date_text.text = latestLaunch.tbd?.let { tbd ->
            latestLaunch.launchDateUnix.formatDateMillisLong(tbd)
        } ?: latestLaunch.launchDateUnix.formatDateMillisLong()

        dashboard_latest_card_view.setOnClickListener { _ ->
            findNavController().navigate(
                R.id.action_dashboard_page_fragment_to_launch_details_fragment,
                bundleOf(
                    "launch" to latestLaunch,
                    "title" to latestLaunch.missionName
                ),
                null,
                FragmentNavigatorExtras(dashboard_latest_card_view to latestLaunch.flightNumber.toString())
            )
        }
    }

    override fun updatePinnedList(id: String, pinnedLaunch: LaunchesModel) {
        if (!pinnedKeysArray.contains(id)) {
            pinnedArray.add(pinnedLaunch)
            pinnedKeysArray.add(id)
        }
        pinnedAdapter.notifyDataSetChanged()
    }

    override fun setCountdown(time: Long) {
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                presenter?.updateCountdown(millisUntilFinished)
            }

            override fun onFinish() {

            }
        }

        countdownTimer?.start()
    }

    override fun updateCountdown(countdown: String) {
        dashboard_countdown_text?.text = countdown
    }

    override fun showPinnedMessage() {
        if (pinnedArray.isEmpty()) dashboard_pinned_message_text.visibility = View.VISIBLE
    }

    override fun hidePinnedMessage() {
        dashboard_pinned_message_text.visibility = View.GONE
    }

    override fun showProgress() {
        dashboard_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        dashboard_progress_bar.visibility = View.GONE
    }

    override fun showCountdown() {
        dashboard_countdown_text.visibility = View.VISIBLE
    }

    override fun hideCountdown() {
        dashboard_countdown_text.visibility = View.GONE
    }

    override fun showNextLaunch() {
        dashboard_next_layout.visibility = View.VISIBLE
    }

    override fun hideNextLaunch() {
        dashboard_next_layout.visibility = View.GONE
    }

    override fun showLatestLaunch() {
        dashboard_latest_layout.visibility = View.VISIBLE
    }

    override fun hideLatestLaunch() {
        dashboard_latest_layout.visibility = View.GONE
    }

    override fun showPinnedList() {
        dashboard_pinned_layout.visibility = View.VISIBLE
    }

    override fun hidePinnedList() {
        dashboard_pinned_layout.visibility = View.GONE
    }

    override fun toggleSwipeProgress(isRefreshing: Boolean) {
        dashboard_swipe_refresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (nextLaunchModel == null
                || latestLaunchModel == null
                || pinnedArray.isEmpty()
                || dashboard_progress_bar.visibility == View.VISIBLE
            ) presenter?.getLatestLaunches()
        }
    }
}
