package uk.co.zac_h.spacex.dashboard

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.dashboard.adapters.DashboardPinnedAdapter
import uk.co.zac_h.spacex.databinding.FragmentDashboardBinding
import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedModel
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelperImpl
import uk.co.zac_h.spacex.utils.formatDateMillisLong
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_LATEST_NEWS
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_NEXT_LAUNCH
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_PINNED_LAUNCH
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_PREVIOUS_LAUNCH

class DashboardFragment : Fragment(), DashboardContract.DashboardView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private var presenter: DashboardContract.DashboardPresenter? = null
    private lateinit var pinnedSharedPreferences: PinnedSharedPreferencesHelper

    private var nextLaunchModel: LaunchesExtendedModel? = null
    private var latestLaunchModel: LaunchesExtendedModel? = null

    private lateinit var pinnedAdapter: DashboardPinnedAdapter

    private lateinit var pinnedArray: ArrayList<LaunchesExtendedModel>
    private lateinit var pinnedKeysArray: ArrayList<String>

    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            nextLaunchModel = it.getParcelable("next")
            latestLaunchModel = it.getParcelable("latest")
        }

        pinnedArray = savedInstanceState?.let {
            it.getParcelableArrayList<LaunchesExtendedModel>("pinned") as ArrayList<LaunchesExtendedModel>
        } ?: ArrayList()

        pinnedKeysArray = savedInstanceState?.getStringArrayList("pinned_keys") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding.toolbar.setupWithNavController(navController, appBarConfig)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit -> {
                    findNavController().navigate(R.id.action_dashboard_page_fragment_to_dashboard_edit_dialog)
                    true
                }
                else -> false
            }
        }

        togglePinnedProgress(false)

        val pinnedPrefs = (context?.applicationContext as App).pinnedPreferencesRepo

        pinnedSharedPreferences = PinnedSharedPreferencesHelperImpl(
            context?.getSharedPreferences("pinned", Context.MODE_PRIVATE)
        )

        presenter = DashboardPresenterImpl(this, DashboardInteractorImpl())

        pinnedAdapter = DashboardPinnedAdapter(context, pinnedArray)

        binding.dashboardPinnedLayout.dashboardPinnedLaunchesRecycler.apply {
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
                        PREFERENCES_PREVIOUS_LAUNCH ->
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
                it.forEach { e ->
                    if (e.key.length < 4) {
                        pinnedSharedPreferences.removePinnedLaunch(e.key)
                        return@forEach
                    }

                    if (e.value as Boolean) {
                        if (!pinnedKeysArray.contains(e.key)) {
                            presenter?.getSingleLaunch(e.key)
                        }
                    } else {
                        if (pinnedKeysArray.contains(e.key)) {
                            if (pinnedArray.size >= pinnedKeysArray.indexOf(e.key)) {
                                pinnedArray.removeAt(pinnedKeysArray.indexOf(e.key))
                                pinnedAdapter.notifyItemRemoved(pinnedKeysArray.indexOf(e.key))
                            }
                            pinnedKeysArray.remove(e.key)
                        }
                        pinnedSharedPreferences.removePinnedLaunch(e.key)
                    }
                }

                if (it.isNullOrEmpty()) showPinnedMessage()
            }
        })

        binding.dashboardSwipeRefresh.setOnRefreshListener {
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
        binding.dashboardSwipeRefresh.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        binding.dashboardSwipeRefresh.isEnabled = false
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
        _binding = null
    }

    override fun updateNextLaunch(nextLaunch: LaunchesExtendedModel) {
        nextLaunchModel = nextLaunch

        binding.dashboardNextLayout.dashboardNextLayout.transitionName = nextLaunch.id

        Glide.with(this)
            .load(nextLaunch.links?.missionPatch?.patchSmall)
            .error(context?.let {
                ContextCompat.getDrawable(it, R.drawable.ic_mission_patch)
            })
            .fallback(context?.let {
                ContextCompat.getDrawable(it, R.drawable.ic_mission_patch)
            })
            .placeholder(context?.let {
                ContextCompat.getDrawable(it, R.drawable.ic_mission_patch)
            })
            .into(binding.dashboardNextLayout.dashboardNextMissionPatchImage)

        binding.dashboardNextLayout.dashboardNextFlightNoText.text =
            context?.getString(R.string.flight_number, nextLaunch.flightNumber)

        binding.dashboardNextLayout.dashboardNextVehicleText.text = nextLaunch.rocket?.name

        binding.dashboardNextLayout.dashboardNextMissionNameText.text = nextLaunch.missionName
        binding.dashboardNextLayout.dashboardNextDateText.text =
            nextLaunch.tbd?.let { nextLaunch.launchDateUnix?.formatDateMillisLong(it) }

        binding.dashboardNextLayout.dashboardNextLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_dashboard_page_fragment_to_launch_details_container_fragment,
                bundleOf(
                    "launch_short" to nextLaunch
                ),
                null,
                FragmentNavigatorExtras(binding.dashboardNextLayout.dashboardNextLayout to nextLaunch.id)
            )
        }
    }

    override fun updateLatestLaunch(latestLaunch: LaunchesExtendedModel) {
        latestLaunchModel = latestLaunch

        binding.dashboardLatestLayout.dashboardLatestLayout.transitionName = latestLaunch.id

        Glide.with(this)
            .load(latestLaunch.links?.missionPatch?.patchSmall)
            .error(context?.let {
                ContextCompat.getDrawable(it, R.drawable.ic_mission_patch)
            })
            .fallback(context?.let {
                ContextCompat.getDrawable(it, R.drawable.ic_mission_patch)
            })
            .placeholder(context?.let {
                ContextCompat.getDrawable(it, R.drawable.ic_mission_patch)
            })
            .into(binding.dashboardLatestLayout.dashboardLatestMissionPatchImage)

        binding.dashboardLatestLayout.dashboardLatestFlightNoText.text =
            context?.getString(R.string.flight_number, latestLaunch.flightNumber)

        binding.dashboardLatestLayout.dashboardLatestVehicleText.text = latestLaunch.rocket?.name

        binding.dashboardLatestLayout.dashboardLatestMissionNameText.text = latestLaunch.missionName

        binding.dashboardLatestLayout.dashboardLatestDateText.text =
            latestLaunch.tbd?.let { latestLaunch.launchDateUnix?.formatDateMillisLong(it) }

        binding.dashboardLatestLayout.dashboardLatestLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_dashboard_page_fragment_to_launch_details_container_fragment,
                bundleOf(
                    "launch_short" to latestLaunch
                ),
                null,
                FragmentNavigatorExtras(binding.dashboardLatestLayout.dashboardLatestLayout to latestLaunch.id)
            )
        }
    }

    override fun updatePinnedList(id: String, pinnedLaunch: LaunchesExtendedModel) {
        if (!pinnedKeysArray.contains(id)) {
            pinnedArray.add(pinnedLaunch)
            pinnedKeysArray.add(id)
        }

        pinnedKeysArray.sortDescending()
        pinnedArray.sortByDescending { it.flightNumber }

        pinnedAdapter.notifyDataSetChanged()
    }

    override fun setCountdown(time: Long) {
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                presenter?.updateCountdown(millisUntilFinished)
            }

            override fun onFinish() {
                nextLaunchModel?.links?.webcast?.let { link ->
                    binding.dashboardNextLayout.dashboardCountdownText.apply {
                        text = context.getString(R.string.watch_live_label)
                        setOnClickListener {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                        }
                    }
                }
            }
        }

        countdownTimer?.start()
    }

    override fun updateCountdown(countdown: String) {
        binding.dashboardNextLayout.dashboardCountdownText.text = countdown
    }

    override fun showPinnedMessage() {
        if (pinnedArray.isEmpty())
            binding.dashboardPinnedLayout.dashboardPinnedMessageText.visibility = View.VISIBLE
    }

    override fun hidePinnedMessage() {
        binding.dashboardPinnedLayout.dashboardPinnedMessageText.visibility = View.GONE
    }

    override fun toggleNextProgress(isShown: Boolean) = when {
        isShown -> binding.dashboardNextLayout.nextProgressIndicator.show()
        else -> binding.dashboardNextLayout.nextProgressIndicator.hide()
    }

    override fun toggleLatestProgress(isShown: Boolean) = when {
        isShown -> binding.dashboardLatestLayout.latestProgressIndicator.show()
        else -> binding.dashboardLatestLayout.latestProgressIndicator.hide()
    }

    override fun togglePinnedProgress(isShown: Boolean) = when {
        isShown -> binding.dashboardPinnedLayout.pinnedProgressIndicator.show()
        else -> binding.dashboardPinnedLayout.pinnedProgressIndicator.hide()
    }

    override fun showCountdown() {
        binding.dashboardNextLayout.dashboardCountdownText.visibility = View.VISIBLE
    }

    override fun hideCountdown() {
        binding.dashboardNextLayout.dashboardCountdownText.visibility = View.GONE
    }

    override fun showNextLaunch() {
        binding.dashboardNextLayout.dashboardNextLayout.visibility = View.VISIBLE
    }

    override fun hideNextLaunch() {
        binding.dashboardNextLayout.dashboardNextLayout.visibility = View.GONE
    }

    override fun showLatestLaunch() {
        binding.dashboardLatestLayout.dashboardLatestLayout.visibility = View.VISIBLE
    }

    override fun hideLatestLaunch() {
        binding.dashboardLatestLayout.dashboardLatestLayout.visibility = View.GONE
    }

    override fun showPinnedList() {
        binding.dashboardPinnedLayout.dashboardPinnedCard.visibility = View.VISIBLE
    }

    override fun hidePinnedList() {
        binding.dashboardPinnedLayout.dashboardPinnedCard.visibility = View.GONE
    }

    override fun toggleSwipeProgress(isRefreshing: Boolean) {
        binding.dashboardSwipeRefresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (nextLaunchModel == null
                || latestLaunchModel == null
                || pinnedArray.isEmpty()
                || binding.dashboardNextLayout.nextProgressIndicator.isShown
                || binding.dashboardLatestLayout.latestProgressIndicator.isShown
            ) presenter?.getLatestLaunches()
        }
    }
}
