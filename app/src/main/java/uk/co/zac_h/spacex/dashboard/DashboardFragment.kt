package uk.co.zac_h.spacex.dashboard

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.*
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentDashboardBinding
import uk.co.zac_h.spacex.databinding.ListItemDashboardLaunchBinding
import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.types.Upcoming
import uk.co.zac_h.spacex.utils.*

class DashboardFragment : BaseFragment() {

    private val viewModel: DashboardViewModel by viewModels()

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = checkNotNull(_binding) { "Binding is null" }

    private lateinit var pinnedAdapter: LaunchesAdapter

    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDashboardBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.doOnPreDraw { startPostponedEnterTransition() }
        postponeEnterTransition()

        viewModel.getLaunches()

        pinnedAdapter = LaunchesAdapter()

        binding.pinned.pinnedRecycler.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            adapter = pinnedAdapter
        }

        binding.refresh.setOnRefreshListener {
            viewModel.getLaunches(CachePolicy.REFRESH)
        }

        viewModel.dashboardLiveData.observe(viewLifecycleOwner) { entries ->
            entries?.forEach {
                when (it.key) {
                    PREFERENCES_NEXT_LAUNCH -> toggleCardVisibility(
                        binding.next.dashboardLaunch,
                        it.value as Boolean
                    )
                    PREFERENCES_PREVIOUS_LAUNCH -> toggleCardVisibility(
                        binding.latest.dashboardLaunch,
                        it.value as Boolean
                    )
                    PREFERENCES_PINNED_LAUNCH -> toggleCardVisibility(
                        binding.pinned.dashboardPinned,
                        it.value as Boolean
                    )
                }
            }
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
            when (response.status) {
                ApiResult.Status.PENDING -> binding.pinned.progress.show()
                ApiResult.Status.SUCCESS -> {
                    pinnedAdapter.submitList(response.data)
                    binding.pinned.progress.hide()
                    binding.pinned.pinnedMessage.visibility =
                        if (response.data.isNullOrEmpty()) View.VISIBLE else View.GONE
                }
                ApiResult.Status.FAILURE -> showError(response.error?.message)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
        countdownTimer = null
        _binding = null
    }

    fun update(data: Any, response: Launch) {
        when (data) {
            Upcoming.NEXT -> update(binding.next, response)
            Upcoming.LATEST -> update(binding.latest, response)
        }
    }

    fun update(binding: ListItemDashboardLaunchBinding, response: Launch) {
        with(binding) {
            dashboardLaunch.transitionName = response.id

            heading.setText(
                if (response.upcoming == true) R.string.next_launch else R.string.latest_launch
            )

            requireContext().loadPatch(response.links?.missionPatch?.patchSmall, missionPatch)

            flightNumber.text = getString(R.string.flight_number, response.flightNumber)
            vehicle.text = response.rocket?.name
            missionName.text = response.missionName
            date.text = response.launchDate?.dateUnix?.formatDateMillisLong(response.datePrecision)

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

    private fun setCountdown(launch: Launch, time: Long) {
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(time: Long) {
                binding.next.countdown.text = String.format(
                    "T-%02d:%02d:%02d:%02d",
                    time.toCountdownDays(),
                    time.toCountdownHours(),
                    time.toCountdownMinutes(),
                    time.toCountdownSeconds()
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

    private fun toggleCardVisibility(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.getLaunches()
    }
}
