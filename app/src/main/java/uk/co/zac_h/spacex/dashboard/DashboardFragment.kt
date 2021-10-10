package uk.co.zac_h.spacex.dashboard

import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.*
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentDashboardBinding
import uk.co.zac_h.spacex.databinding.ListItemDashboardLaunchBinding
import uk.co.zac_h.spacex.dto.spacex.Launch
import uk.co.zac_h.spacex.dto.spacex.Upcoming
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.utils.*

class DashboardFragment : BaseFragment() {

    override val title: String by lazy { getString(R.string.menu_home) }

    private val viewModel: DashboardViewModel by viewModels()

    private lateinit var binding: FragmentDashboardBinding

    private lateinit var pinnedAdapter: LaunchesAdapter
    private var pinnedArray: ArrayList<Launch> = ArrayList()

    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        exitTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
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

        viewModel.getLaunch("next")
        viewModel.getLaunch("latest")

        pinnedAdapter = LaunchesAdapter(requireContext()).also {
            it.submitList(pinnedArray)
        }

        binding.pinned.pinnedRecycler.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            adapter = pinnedAdapter
        }

        binding.refresh.setOnRefreshListener {
            viewModel.getLaunch("next", CachePolicy.REFRESH)
            viewModel.getLaunch("latest", CachePolicy.REFRESH)
        }

        viewModel.dashboardLiveData.observe(viewLifecycleOwner) { entries ->
            entries?.forEach {
                when (it.key) {
                    PREFERENCES_NEXT_LAUNCH -> showNextLaunch(it.value as Boolean)
                    PREFERENCES_PREVIOUS_LAUNCH -> showLatestLaunch(it.value as Boolean)
                    PREFERENCES_PINNED_LAUNCH -> showPinnedList(it.value as Boolean)
                }
            }
        }

        viewModel.pinnedLiveData.observe(viewLifecycleOwner) { mode ->
            mode?.forEach { e ->
                when (e.value) {
                    true -> if (pinnedArray.none { it.id == e.key }) {
                        viewModel.getLaunch(e.key)
                    }
                    false -> {
                        pinnedArray.removeAll { it.id == e.key }
                        pinnedAdapter.submitList(pinnedArray)
                        //pinnedSharedPreferences.removePinnedLaunch(e.key)
                    }
                }
            }

            if (mode.isNullOrEmpty()) showPinnedMessage()
        }

        viewModel.nextLaunch.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                ApiResult.Status.PENDING -> binding.next.progress.show()
                ApiResult.Status.SUCCESS -> {
                    binding.refresh.isRefreshing = false
                    binding.next.progress.hide()
                    response.data?.let {
                        val time = it.launchDate?.dateUnix?.times(1000)
                            ?.minus(System.currentTimeMillis()) ?: 0
                        if (it.tbd == false && time >= 0) {
                            setCountdown(it, time)
                            binding.next.countdown.visibility = View.VISIBLE
                            binding.next.heading.visibility = View.GONE
                        } else {
                            binding.next.countdown.visibility = View.GONE
                            binding.next.heading.visibility = View.VISIBLE
                        }
                        update(Upcoming.NEXT, it)
                    }
                }
                ApiResult.Status.FAILURE -> {
                    binding.refresh.isRefreshing = false
                    showError(response.error?.message)
                }
            }
        }

        viewModel.latestLaunch.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                ApiResult.Status.PENDING -> binding.latest.progress.show()
                ApiResult.Status.SUCCESS -> {
                    binding.refresh.isRefreshing = false
                    binding.latest.progress.hide()
                    response.data?.let { update(Upcoming.LATEST, it) }
                }
                ApiResult.Status.FAILURE -> {
                    binding.refresh.isRefreshing = false
                    showError(response.error?.message)
                }
            }
        }

        viewModel.pinnedLaunches.observe(viewLifecycleOwner) { response ->
            val launches = response.map { it.data }
            pinnedAdapter.submitList(launches)
            binding.pinned.progress.hide()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
        countdownTimer = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.edit -> {
            //findNavController().navigate(R.id.action_dashboard_page_fragment_to_dashboard_edit_dialog)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    fun update(data: Any, response: Launch) {
        when (data) {
            Upcoming.NEXT -> update(binding.next, response)
            Upcoming.LATEST -> update(binding.latest, response)
            else -> updatePinnedList(data as String, response)
        }
    }

    fun update(binding: ListItemDashboardLaunchBinding, response: Launch) {
        with(binding) {
            dashboardLaunch.transitionName = response.id

            heading.setText(
                if (response.upcoming == true) R.string.next_launch else R.string.latest_launch
            )

            Glide.with(this@DashboardFragment)
                .load(response.links?.missionPatch?.patchSmall)
                .error(getDrawable(requireContext(), R.drawable.ic_mission_patch))
                .fallback(getDrawable(requireContext(), R.drawable.ic_mission_patch))
                .placeholder(getDrawable(requireContext(), R.drawable.ic_mission_patch))
                .into(missionPatch)

            flightNumber.text = getString(R.string.flight_number, response.flightNumber)

            vehicle.text = response.rocket?.name

            missionName.text = response.missionName

            date.text =
                response.launchDate?.dateUnix?.formatDateMillisLong(/*response.datePrecision*/)

            dashboardLaunch.let { card ->
                card.setOnClickListener {
                    findNavController().navigate(
                        NavGraphDirections.actionLaunchItemToLaunchDetailsContainer(
                            response.missionName,
                            response.id
                        ),
                        FragmentNavigatorExtras(card to response.id)
                    )
                }
            }
        }
    }

    private fun updatePinnedList(id: String, pinnedLaunch: Launch) {
        if (pinnedArray.none { it.id == id }) pinnedArray.add(pinnedLaunch)

        pinnedArray.sortByDescending { it.flightNumber }

        pinnedAdapter.submitList(pinnedArray)
    }

    private fun setCountdown(launch: Launch, time: Long) {
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(time: Long) {
                updateCountdown(
                    String.format(
                        "T-%02d:%02d:%02d:%02d",
                        time.toCountdownDays(),
                        time.toCountdownHours(),
                        time.toCountdownMinutes(),
                        time.toCountdownSeconds()
                    )
                )
            }

            override fun onFinish() {
                launch.links?.webcast?.let { link ->
                    binding.next.countdown.visibility = View.GONE
                    binding.next.watchNow.apply {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            openWebLink(link)
                        }
                    }
                }
            }
        }

        countdownTimer?.start()
    }

    fun updateCountdown(countdown: String) {
        binding.next.countdown.text = countdown
    }

    private fun showPinnedMessage() {
        binding.pinned.progress.hide()
        if (pinnedArray.isEmpty()) binding.pinned.pinnedMessage.visibility = View.VISIBLE
    }

    fun hidePinnedMessage() {
        binding.pinned.pinnedMessage.visibility = View.GONE
    }

    private fun showNextLaunch(visible: Boolean) {
        binding.next.dashboardLaunch.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun showLatestLaunch(visible: Boolean) {
        binding.latest.dashboardLaunch.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun showPinnedList(visible: Boolean) {
        binding.pinned.dashboardPinned.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.getLaunch("next")
        viewModel.getLaunch("latest")
    }
}
