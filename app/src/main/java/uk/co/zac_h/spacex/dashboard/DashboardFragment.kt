package uk.co.zac_h.spacex.dashboard

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
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
import uk.co.zac_h.spacex.databinding.ListItemDashboardLaunchBinding
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelperImpl
import uk.co.zac_h.spacex.utils.formatDateMillisLong
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_LATEST_NEWS
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_NEXT_LAUNCH
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_PINNED_LAUNCH
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_PREVIOUS_LAUNCH

class DashboardFragment : Fragment(),
    DashboardContract.View,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var binding: FragmentDashboardBinding? = null

    private var presenter: DashboardContract.Presenter? = null
    private lateinit var pinnedSharedPreferences: PinnedSharedPreferencesHelper

    private var nextLaunchModel: Launch? = null
    private var latestLaunchModel: Launch? = null

    private lateinit var pinnedAdapter: DashboardPinnedAdapter

    private lateinit var pinnedArray: ArrayList<Launch>

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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        binding?.toolbar?.setOnMenuItemClickListener {
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

        binding?.pinnedLayout?.dashboardPinnedLaunchesRecycler?.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            adapter = pinnedAdapter
        }

        val prefs = (context?.applicationContext as App).dashboardPreferencesRepo

        prefs.visibleLive.observe(viewLifecycleOwner, { mode ->
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

        pinnedPrefs.pinnedLive.observe(viewLifecycleOwner, { mode ->
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
                        pinnedAdapter.notifyDataSetChanged()
                        pinnedSharedPreferences.removePinnedLaunch(e.key)
                    }
                }

                if (map.isNullOrEmpty()) showPinnedMessage()
            }
        })

        binding?.dashboardSwipeRefresh?.setOnRefreshListener {
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
        binding?.dashboardSwipeRefresh?.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        binding?.dashboardSwipeRefresh?.isEnabled = false
    }

    override fun onStop() {
        super.onStop()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            nextLaunchModel?.let { putParcelable("next", it) }
            latestLaunchModel?.let { putParcelable("latest", it) }
            putParcelableArrayList("pinned", pinnedArray)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
        countdownTimer = null
        presenter?.cancelRequest()
        binding = null
    }

    override fun update(data: Any, response: Launch) {
        when (data as String) {
            "next" -> {
                nextLaunchModel = response
                update(binding?.nextLayout, response)
            }
            "latest" -> {
                latestLaunchModel = response
                update(binding?.latestLayout, response)
            }
            else -> updatePinnedList(data, response)
        }
    }

    fun update(binding: ListItemDashboardLaunchBinding?, response: Launch) {
        binding?.dashboardLaunchLayout?.transitionName = response.id

        when (response.upcoming) {
            true -> binding?.headingText?.text = context?.getString(R.string.next_launch)
            false -> binding?.headingText?.text = context?.getString(R.string.latest_launch)
        }

        binding?.missionPatchImage?.let {
            Glide.with(this)
                .load(response.links?.missionPatch?.patchSmall)
                .error(context?.let { ctx ->
                    ContextCompat.getDrawable(ctx, R.drawable.ic_mission_patch)
                })
                .fallback(context?.let { ctx ->
                    ContextCompat.getDrawable(ctx, R.drawable.ic_mission_patch)
                })
                .placeholder(context?.let { ctx ->
                    ContextCompat.getDrawable(ctx, R.drawable.ic_mission_patch)
                })
                .into(it)
        }

        binding?.flightNoText?.text =
            context?.getString(R.string.flight_number, response.flightNumber)

        binding?.vehicleText?.text = response.rocket?.name

        binding?.missionNameText?.text = response.missionName
        binding?.dateText?.text =
            response.datePrecision?.let { response.launchDate?.dateUnix?.formatDateMillisLong(it) }

        binding?.dashboardLaunchLayout?.let { card ->
            card.setOnClickListener {
                findNavController().navigate(
                    R.id.action_dashboard_page_fragment_to_launch_details_container_fragment,
                    bundleOf(
                        "launch_short" to response
                    ),
                    null,
                    FragmentNavigatorExtras(card to response.id)
                )
            }
        }
    }

    private fun updatePinnedList(id: String, pinnedLaunch: Launch) {
        if (pinnedArray.none { it.id == id }) {
            pinnedArray.add(pinnedLaunch)
        }

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
                    binding?.nextLayout?.countdownText?.apply {
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
        binding?.nextLayout?.countdownText?.text = countdown
    }

    override fun showPinnedMessage() {
        if (pinnedArray.isEmpty())
            binding?.pinnedLayout?.dashboardPinnedMessageText?.visibility = View.VISIBLE
    }

    override fun hidePinnedMessage() {
        binding?.pinnedLayout?.dashboardPinnedMessageText?.visibility = View.GONE
    }

    override fun toggleNextProgress(isShown: Boolean) = when {
        isShown -> binding?.nextLayout?.progressIndicator?.show()
        else -> binding?.nextLayout?.progressIndicator?.hide()
    }

    override fun toggleLatestProgress(isShown: Boolean) = when {
        isShown -> binding?.latestLayout?.progressIndicator?.show()
        else -> binding?.latestLayout?.progressIndicator?.hide()
    }

    override fun togglePinnedProgress(isShown: Boolean) = when {
        isShown -> binding?.pinnedLayout?.pinnedProgressIndicator?.show()
        else -> binding?.pinnedLayout?.pinnedProgressIndicator?.hide()
    }

    override fun showCountdown() {
        binding?.nextLayout?.countdownText?.visibility = View.VISIBLE
    }

    override fun hideCountdown() {
        binding?.nextLayout?.countdownText?.visibility = View.GONE
    }

    override fun showNextHeading() {
        binding?.nextLayout?.headingText?.visibility = View.VISIBLE
    }

    override fun hideNextHeading() {
        binding?.nextLayout?.headingText?.visibility = View.GONE
    }

    override fun showNextLaunch() {
        binding?.nextLayout?.dashboardLaunchLayout?.visibility = View.VISIBLE
    }

    override fun hideNextLaunch() {
        binding?.nextLayout?.dashboardLaunchLayout?.visibility = View.GONE
    }

    override fun showLatestLaunch() {
        binding?.latestLayout?.dashboardLaunchLayout?.visibility = View.VISIBLE
    }

    override fun hideLatestLaunch() {
        binding?.latestLayout?.dashboardLaunchLayout?.visibility = View.GONE
    }

    override fun showPinnedList() {
        binding?.pinnedLayout?.dashboardPinnedCard?.visibility = View.VISIBLE
    }

    override fun hidePinnedList() {
        binding?.pinnedLayout?.dashboardPinnedCard?.visibility = View.GONE
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding?.dashboardSwipeRefresh?.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            binding?.let { binding ->
                if (nextLaunchModel == null
                    || latestLaunchModel == null
                    || pinnedArray.isEmpty()
                    || binding.nextLayout.progressIndicator.isShown
                    || binding.latestLayout.progressIndicator.isShown
                ) presenter?.getLatestLaunches()
            }
        }
    }
}
