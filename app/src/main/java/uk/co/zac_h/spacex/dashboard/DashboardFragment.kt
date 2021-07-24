package uk.co.zac_h.spacex.dashboard

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.LinearProgressIndicator
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentDashboardBinding
import uk.co.zac_h.spacex.databinding.ListItemDashboardLaunchBinding
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.model.spacex.Upcoming
import uk.co.zac_h.spacex.utils.*
import uk.co.zac_h.spacex.utils.Keys.DashboardKeys
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_LATEST_NEWS
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_NEXT_LAUNCH
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_PINNED_LAUNCH
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_PREVIOUS_LAUNCH

class DashboardFragment : BaseFragment(), DashboardContract.View {

    override val title: String by lazy { getString(R.string.menu_home) }

    private lateinit var binding: FragmentDashboardBinding

    private var presenter: DashboardContract.Presenter? = null

    private lateinit var pinnedSharedPreferences: PinnedSharedPreferencesHelper

    private var nextLaunchModel: Launch? = null
    private var latestLaunchModel: Launch? = null
    private lateinit var pinnedAdapter: LaunchesAdapter
    private lateinit var pinnedArray: ArrayList<Launch>

    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            nextLaunchModel = it.getParcelable(DashboardKeys.NEXT_SAVED_STATE)
            latestLaunchModel = it.getParcelable(DashboardKeys.LATEST_SAVED_STATE)
        }

        pinnedArray = savedInstanceState?.let {
            it.getParcelableArrayList<Launch>(DashboardKeys.PINNED_SAVED_STATE) as ArrayList<Launch>
        } ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDashboardBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.doOnPreDraw { startPostponedEnterTransition() }
        postponeEnterTransition()

        binding.toolbarLayout.progress.hide()
        binding.toolbarLayout.toolbar.apply {
            setup()
            createOptionsMenu(R.menu.menu_dashboard)
        }

        togglePinnedProgress(false)

        val pinnedPrefs = (requireContext().applicationContext as App).pinnedPreferencesRepo

        pinnedSharedPreferences = PinnedSharedPreferencesHelperImpl(
            requireContext().getSharedPreferences("pinned", Context.MODE_PRIVATE)
        )

        presenter = DashboardPresenterImpl(this, DashboardInteractorImpl())

        pinnedAdapter = LaunchesAdapter(requireContext())

        binding.pinned.pinnedRecycler.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            adapter = pinnedAdapter
        }

        val prefs = (requireContext().applicationContext as App).dashboardPreferencesRepo

        prefs.visibleLive.observe(viewLifecycleOwner) { mode ->
            mode?.let {
                it.forEach { elements ->
                    when (elements.key) {
                        PREFERENCES_NEXT_LAUNCH ->
                            presenter?.toggleNextLaunchVisibility(elements.value as Boolean)
                        PREFERENCES_PREVIOUS_LAUNCH ->
                            presenter?.toggleLatestLaunchVisibility(elements.value as Boolean)
                        PREFERENCES_PINNED_LAUNCH ->
                            presenter?.togglePinnedList(elements.value as Boolean)
                        PREFERENCES_LATEST_NEWS -> {
                        }
                    }
                }
            }
        }

        pinnedPrefs.pinnedLive.observe(viewLifecycleOwner) { mode ->
            mode?.let { map ->
                map.forEach { e ->
                    if (e.key.length < 4) {
                        pinnedSharedPreferences.removePinnedLaunch(e.key)
                        return@forEach
                    }

                    if (e.value as Boolean) {
                        if (pinnedArray.none { it.id == e.key }) {
                            presenter?.get(e.key)
                        }
                    } else {
                        pinnedArray.removeAll { it.id == e.key }
                        pinnedAdapter.update(pinnedArray)
                        pinnedSharedPreferences.removePinnedLaunch(e.key)
                    }
                }

                if (map.isNullOrEmpty()) showPinnedMessage()
            }
        }

        binding.refresh.setOnRefreshListener {
            apiState = ApiState.PENDING
            presenter?.getLatestLaunches()
        }

        presenter?.getLatestLaunches(nextLaunchModel, latestLaunchModel)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            nextLaunchModel?.let { putParcelable(DashboardKeys.NEXT_SAVED_STATE, it) }
            latestLaunchModel?.let { putParcelable(DashboardKeys.LATEST_SAVED_STATE, it) }
            putParcelableArrayList(DashboardKeys.PINNED_SAVED_STATE, pinnedArray)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
        countdownTimer = null
        presenter?.cancelRequest()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.edit -> {
            findNavController().navigate(R.id.action_dashboard_page_fragment_to_dashboard_edit_dialog)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun update(data: Any, response: Launch) {
        if (apiState != ApiState.FAILED) apiState = ApiState.SUCCESS
        when (data) {
            Upcoming.NEXT -> {
                nextLaunchModel = response
                update(binding.next, response)
            }
            Upcoming.LATEST -> {
                latestLaunchModel = response
                update(binding.latest, response)
            }
            else -> updatePinnedList(data as String, response)
        }
    }

    fun update(binding: ListItemDashboardLaunchBinding, response: Launch) {
        if (apiState != ApiState.FAILED) apiState = ApiState.SUCCESS
        with(binding) {
            dashboardLaunch.transitionName = response.id

            when (response.upcoming) {
                true -> heading.text = getString(R.string.next_launch)
                false -> heading.text = getString(R.string.latest_launch)
            }

            missionPatch.let {
                Glide.with(this@DashboardFragment)
                    .load(response.links?.missionPatch?.patchSmall)
                    .error(getDrawable(requireContext(), R.drawable.ic_mission_patch))
                    .fallback(getDrawable(requireContext(), R.drawable.ic_mission_patch))
                    .placeholder(getDrawable(requireContext(), R.drawable.ic_mission_patch))
                    .into(it)
            }

            flightNumber.text = getString(R.string.flight_number, response.flightNumber)

            vehicle.text = response.rocket?.name

            missionName.text = response.missionName

            date.text = response.datePrecision?.let {
                response.launchDate?.dateUnix?.formatDateMillisLong(it)
            }

            dashboardLaunch.let { card ->
                card.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_launch_item_to_launch_details_container_fragment,
                        bundleOf(LAUNCH_SHORT_KEY to response),
                        null,
                        FragmentNavigatorExtras(card to response.id)
                    )
                }
            }
        }
    }

    private fun updatePinnedList(id: String, pinnedLaunch: Launch) {
        if (pinnedArray.none { it.id == id }) pinnedArray.add(pinnedLaunch)

        pinnedArray.sortByDescending { it.flightNumber }

        pinnedAdapter.update(pinnedArray)
    }

    override fun setCountdown(time: Long) {
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                presenter?.updateCountdown(millisUntilFinished)
            }

            override fun onFinish() {
                nextLaunchModel?.links?.webcast?.let { link ->
                    binding.next.countdown.apply {
                        text = getString(R.string.watch_live_label)
                        setOnClickListener {
                            openWebLink(link)
                        }
                    }
                }
            }
        }

        countdownTimer?.start()
    }

    override fun updateCountdown(countdown: String) {
        binding.next.countdown.text = countdown
    }

    override fun showPinnedMessage() {
        if (pinnedArray.isEmpty()) binding.pinned.pinnedMessage.visibility = View.VISIBLE
    }

    override fun hidePinnedMessage() {
        binding.pinned.pinnedMessage.visibility = View.GONE
    }

    override fun toggleNextProgress(isShown: Boolean) = when (isShown) {
        true -> binding.next.progress.show()
        false -> binding.next.progress.hide()
    }

    override fun toggleLatestProgress(isShown: Boolean) = when (isShown) {
        true -> binding.latest.progress.show()
        false -> binding.latest.progress.hide()
    }

    override fun togglePinnedProgress(isShown: Boolean) = when (isShown) {
        true -> binding.pinned.progress.show()
        false -> binding.pinned.progress.hide()
    }

    override fun showCountdown() {
        binding.next.countdown.visibility = View.VISIBLE
    }

    override fun hideCountdown() {
        binding.next.countdown.visibility = View.GONE
    }

    override fun showNextHeading() {
        binding.next.heading.visibility = View.VISIBLE
    }

    override fun hideNextHeading() {
        binding.next.heading.visibility = View.GONE
    }

    override fun showNextLaunch() {
        binding.next.dashboardLaunch.visibility = View.VISIBLE
    }

    override fun hideNextLaunch() {
        binding.next.dashboardLaunch.visibility = View.GONE
    }

    override fun showLatestLaunch() {
        binding.latest.dashboardLaunch.visibility = View.VISIBLE
    }

    override fun hideLatestLaunch() {
        binding.latest.dashboardLaunch.visibility = View.GONE
    }

    override fun showPinnedList() {
        binding.pinned.dashboardPinned.visibility = View.VISIBLE
    }

    override fun hidePinnedList() {
        binding.pinned.dashboardPinned.visibility = View.GONE
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.refresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        apiState = ApiState.FAILED
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        when (apiState) {
            ApiState.PENDING, ApiState.FAILED -> presenter?.getLatestLaunches()
            ApiState.SUCCESS -> Log.i(title, "Network available and data loaded")
        }
    }
}
