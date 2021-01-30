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
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentDashboardBinding
import uk.co.zac_h.spacex.databinding.ListItemDashboardLaunchBinding
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.utils.LAUNCH_SHORT_KEY
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelperImpl
import uk.co.zac_h.spacex.utils.formatDateMillisLong
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_LATEST_NEWS
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_NEXT_LAUNCH
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_PINNED_LAUNCH
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_PREVIOUS_LAUNCH

class DashboardFragment : BaseFragment(), DashboardContract.View {

    companion object {
        const val NEXT_KEY = "next"
        const val LATEST_KEY = "latest"
        const val PINNED_KEY = "pinned"
    }

    override var title: String = "Dashboard"

    private var binding: FragmentDashboardBinding? = null

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
            nextLaunchModel = it.getParcelable(NEXT_KEY)
            latestLaunchModel = it.getParcelable(LATEST_KEY)
        }

        pinnedArray = savedInstanceState?.let {
            it.getParcelableArrayList<Launch>(PINNED_KEY) as ArrayList<Launch>
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

        pinnedAdapter = LaunchesAdapter(context, pinnedArray)

        binding?.pinned?.pinnedRecycler?.apply {
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

        binding?.refresh?.setOnRefreshListener {
            presenter?.getLatestLaunches()
        }

        presenter?.getLatestLaunches(nextLaunchModel, latestLaunchModel)
    }

    override fun onResume() {
        super.onResume()
        binding?.refresh?.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        binding?.refresh?.isEnabled = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            nextLaunchModel?.let { putParcelable(NEXT_KEY, it) }
            latestLaunchModel?.let { putParcelable(LATEST_KEY, it) }
            putParcelableArrayList(PINNED_KEY, pinnedArray)
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
            NEXT_KEY -> {
                nextLaunchModel = response
                update(binding?.next, response)
            }
            LATEST_KEY -> {
                latestLaunchModel = response
                update(binding?.latest, response)
            }
            else -> updatePinnedList(data, response)
        }
    }

    fun update(binding: ListItemDashboardLaunchBinding?, response: Launch) {
        binding?.let {
            binding.dashboardLaunch.transitionName = response.id

            when (response.upcoming) {
                true -> binding.heading.text = context?.getString(R.string.next_launch)
                false -> binding.heading.text = context?.getString(R.string.latest_launch)
            }

            binding.missionPatch.let {
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

            binding.flightNumber.text =
                context?.getString(R.string.flight_number, response.flightNumber)

            binding.vehicle.text = response.rocket?.name

            binding.missionName.text = response.missionName
            binding.date.text =
                response.datePrecision?.let { response.launchDate?.dateUnix?.formatDateMillisLong(it) }

            binding.dashboardLaunch.let { card ->
                card.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_launch_item_to_launch_details_container_fragment,
                        bundleOf(
                            LAUNCH_SHORT_KEY to response
                        ),
                        null,
                        FragmentNavigatorExtras(card to response.id)
                    )
                }
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
                    binding?.next?.countdown?.apply {
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
        binding?.next?.countdown?.text = countdown
    }

    override fun showPinnedMessage() {
        if (pinnedArray.isEmpty()) binding?.pinned?.pinnedMessage?.visibility = View.VISIBLE
    }

    override fun hidePinnedMessage() {
        binding?.pinned?.pinnedMessage?.visibility = View.GONE
    }

    override fun toggleNextProgress(isShown: Boolean) = when {
        isShown -> binding?.next?.progress?.show()
        else -> binding?.next?.progress?.hide()
    }

    override fun toggleLatestProgress(isShown: Boolean) = when {
        isShown -> binding?.latest?.progress?.show()
        else -> binding?.latest?.progress?.hide()
    }

    override fun togglePinnedProgress(isShown: Boolean) = when {
        isShown -> binding?.pinned?.progress?.show()
        else -> binding?.pinned?.progress?.hide()
    }

    override fun showCountdown() {
        binding?.next?.countdown?.visibility = View.VISIBLE
    }

    override fun hideCountdown() {
        binding?.next?.countdown?.visibility = View.GONE
    }

    override fun showNextHeading() {
        binding?.next?.heading?.visibility = View.VISIBLE
    }

    override fun hideNextHeading() {
        binding?.next?.heading?.visibility = View.GONE
    }

    override fun showNextLaunch() {
        binding?.next?.dashboardLaunch?.visibility = View.VISIBLE
    }

    override fun hideNextLaunch() {
        binding?.next?.dashboardLaunch?.visibility = View.GONE
    }

    override fun showLatestLaunch() {
        binding?.latest?.dashboardLaunch?.visibility = View.VISIBLE
    }

    override fun hideLatestLaunch() {
        binding?.latest?.dashboardLaunch?.visibility = View.GONE
    }

    override fun showPinnedList() {
        binding?.pinned?.dashboardPinned?.visibility = View.VISIBLE
    }

    override fun hidePinnedList() {
        binding?.pinned?.dashboardPinned?.visibility = View.GONE
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding?.refresh?.isRefreshing = isRefreshing
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            binding?.let { binding ->
                if (nextLaunchModel == null
                    || latestLaunchModel == null
                    || pinnedArray.isEmpty()
                    || binding.next.progress.isShown
                    || binding.latest.progress.isShown
                ) presenter?.getLatestLaunches()
            }
        }
    }
}
