package uk.co.zac_h.spacex.dashboard

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
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
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.dashboard.adapters.DashboardPinnedAdapter
import uk.co.zac_h.spacex.databinding.FragmentDashboardBinding
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
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
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setSupportActionBar(binding.toolbar)

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding.toolbar.setupWithNavController(navController, appBarConfig)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

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
                it.forEach { element ->
                    if (element.key.length < 4) {
                        pinnedSharedPreferences.removePinnedLaunch(element.key)
                        return@forEach
                    }

                    if (element.value as Boolean) {
                        if (!pinnedKeysArray.contains(element.key)) {
                            presenter?.getSingleLaunch(element.key)
                        }
                    } else {
                        pinnedArray.removeAt(pinnedKeysArray.indexOf(element.key))
                        pinnedAdapter.notifyItemRemoved(pinnedKeysArray.indexOf(element.key))
                        pinnedKeysArray.remove(element.key)
                        pinnedSharedPreferences.removePinnedLaunch(element.key)
                    }
                }
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

        binding.dashboardNextLayout.dashboardNextMissionNameText.text = nextLaunch.missionName
        binding.dashboardNextLayout.dashboardNextDateText.text =
            nextLaunch.launchDateUnix.formatDateMillisLong(nextLaunch.tbd)

        binding.dashboardNextLayout.dashboardNextLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_dashboard_page_fragment_to_launch_details_fragment,
                bundleOf(
                    "launch_id" to nextLaunch.id,
                    "flight_number" to nextLaunch.flightNumber,
                    "title" to nextLaunch.missionName
                ),
                null,
                FragmentNavigatorExtras(binding.dashboardNextLayout.dashboardNextLayout to nextLaunch.id)
            )
        }
    }

    override fun updateLatestLaunch(latestLaunch: LaunchesModel) {
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

        binding.dashboardLatestLayout.dashboardLatestMissionNameText.text = latestLaunch.missionName

        binding.dashboardLatestLayout.dashboardLatestDateText.text =
            latestLaunch.launchDateUnix.formatDateMillisLong(latestLaunch.tbd)

        binding.dashboardLatestLayout.dashboardLatestLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_dashboard_page_fragment_to_launch_details_fragment,
                bundleOf(
                    "launch_id" to latestLaunch.id,
                    "flight_number" to latestLaunch.flightNumber,
                    "title" to latestLaunch.missionName
                ),
                null,
                FragmentNavigatorExtras(binding.dashboardLatestLayout.dashboardLatestLayout to latestLaunch.id)
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
                nextLaunchModel?.links?.webcast?.let { link ->
                    binding.dashboardNextLayout.dashboardCountdownText.apply {
                        text = "WATCH LIVE"
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

    override fun showProgress() {
        binding.dashboardProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.dashboardProgressBar.visibility = View.GONE
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
                || binding.dashboardProgressBar.visibility == View.VISIBLE
            ) presenter?.getLatestLaunches()
        }
    }
}
