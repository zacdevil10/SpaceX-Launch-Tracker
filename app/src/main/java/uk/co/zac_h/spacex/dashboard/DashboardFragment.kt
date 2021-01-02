package uk.co.zac_h.spacex.dashboard

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.dashboard.adapters.DashboardPinnedAdapter
import uk.co.zac_h.spacex.databinding.FragmentDashboardBinding
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelperImpl
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.utils.repo.DashboardObject.PREFERENCES_LATEST_NEWS
import uk.co.zac_h.spacex.utils.repo.DashboardObject.PREFERENCES_NEXT_LAUNCH
import uk.co.zac_h.spacex.utils.repo.DashboardObject.PREFERENCES_PINNED_LAUNCH
import uk.co.zac_h.spacex.utils.repo.DashboardObject.PREFERENCES_PREVIOUS_LAUNCH
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class DashboardFragment : Fragment(), DashboardContract.DashboardView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private val viewModel: DashboardViewModel by viewModels()

    private lateinit var binding: FragmentDashboardBinding

    private var presenter: DashboardContract.DashboardPresenter? = null
    private lateinit var pinnedSharedPreferences: PinnedSharedPreferencesHelper

    private var nextLaunchModel: Launch? = null
    private var latestLaunchModel: Launch? = null

    private lateinit var pinnedAdapter: DashboardPinnedAdapter

    private lateinit var pinnedArray: ArrayList<Launch>
    private lateinit var pinnedKeysArray: ArrayList<String>

    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            nextLaunchModel = it.getParcelable("next")
            latestLaunchModel = it.getParcelable("latest")
        }

        pinnedArray = savedInstanceState?.let {
            it.getParcelableArrayList<Launch>("pinned") as ArrayList<Launch>
        } ?: ArrayList()

        pinnedKeysArray = savedInstanceState?.getStringArrayList("pinned_keys") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDashboardBinding.inflate(inflater, container, false).apply {
        viewModel = this@DashboardFragment.viewModel
        lifecycleOwner = viewLifecycleOwner
        binding = this
    }.root

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

        pinnedSharedPreferences = PinnedSharedPreferencesHelperImpl(
            context?.getSharedPreferences("pinned", Context.MODE_PRIVATE)
        )

        presenter = DashboardPresenterImpl(this, DashboardInteractorImpl())

        pinnedAdapter = DashboardPinnedAdapter(context, pinnedArray)

        binding.dashboardPinned.dashboardPinnedLaunchesRecycler.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            adapter = pinnedAdapter
        }

        viewModel.pinnedIds.observe(viewLifecycleOwner, { mode ->
            mode?.let {
                it.forEach { e ->
                    if (e.key.length < 4) {
                        pinnedSharedPreferences.removePinnedLaunch(e.key)
                        return@forEach
                    }

                    if (e.value) {
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

        viewModel.nextLaunch.observe(viewLifecycleOwner, {
            it.tbd?.let { tbd ->
                val time = (it.launchDate?.dateUnix?.times(1000) ?: 0) - System.currentTimeMillis()
                if (!tbd && time >= 0) {
                    setCountdown(time)
                } else viewModel.countdown.value = null
            } ?: run {
                viewModel.countdown.value = null
            }
        })

        binding.dashboardSwipeRefresh.setOnRefreshListener {
            viewModel.refreshing.value = true
        }
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
        outState.putParcelableArrayList("pinned", pinnedArray)
        outState.putStringArrayList("pinned_keys", pinnedKeysArray)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
        countdownTimer = null
    }

    override fun updateNextLaunch(nextLaunch: Launch) {
        /*binding?.dashboardNext?.dashboardNextLayout?.let { card ->
            card.setOnClickListener {
                findNavController().navigate(
                    R.id.action_dashboard_page_fragment_to_launch_details_container_fragment,
                    bundleOf(
                        "launch_short" to nextLaunch
                    ),
                    null,
                    FragmentNavigatorExtras(card to nextLaunch.id)
                )
            }
        }*/
    }

    override fun updateLatestLaunch(latestLaunch: Launch) {
        /*binding?.dashboardLatest?.dashboardLatestLayout?.let { card ->
            card.setOnClickListener {
                findNavController().navigate(
                    R.id.action_dashboard_page_fragment_to_launch_details_container_fragment,
                    bundleOf(
                        "launch_short" to latestLaunch
                    ),
                    null,
                    FragmentNavigatorExtras(card to latestLaunch.id)
                )
            }
        }*/
    }

    override fun updatePinnedList(id: String, pinnedLaunch: Launch) {
        if (!pinnedKeysArray.contains(id)) {
            pinnedArray.add(pinnedLaunch)
            pinnedKeysArray.add(id)
        }

        pinnedKeysArray.sortDescending()
        pinnedArray.sortByDescending { it.flightNumber }

        pinnedAdapter.notifyDataSetChanged()
    }

    private fun setCountdown(time: Long) {
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                viewModel.countdown.value = String.format(
                    "T-%02d:%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toDays(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(
                        TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                    ),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                    ),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    )
                )
            }

            override fun onFinish() {
                nextLaunchModel?.links?.webcast?.let { link ->
                    viewModel.countdown.value =
                        requireContext().getString(R.string.watch_live_label)
                    /*setOnClickListener {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                    }*/
                }
            }
        }

        countdownTimer?.start()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {

        }
    }
}
