package uk.co.zac_h.spacex.dashboard

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.*
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentDashboardBinding
import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.types.Upcoming
import uk.co.zac_h.spacex.utils.*
import uk.co.zac_h.widget.DashboardView

@AndroidEntryPoint
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
                    PREFERENCES_NEXT_LAUNCH -> binding.next.isVisible = it.value as Boolean
                    PREFERENCES_PREVIOUS_LAUNCH -> binding.latest.isVisible = it.value as Boolean
                    PREFERENCES_PINNED_LAUNCH ->
                        binding.pinned.dashboardPinned.isVisible = it.value as Boolean
                }
            }
        }

        viewModel.nextLaunch.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResult.Pending -> binding.next.progress.show()
                is ApiResult.Success -> {
                    binding.refresh.isRefreshing = false
                    binding.next.progress.hide()
                    response.data?.let {
                        val time = it.launchDate?.dateUnix?.times(1000)
                            ?.minus(System.currentTimeMillis()) ?: 0
                        if (it.tbd == false && time >= 0) {
                            setCountdown(it, time)
                        }
                        update(Upcoming.NEXT, it)
                    }
                }
                is ApiResult.Failure -> {
                    binding.refresh.isRefreshing = false
                    showError(response.exception.message)
                }
            }
        }

        viewModel.latestLaunch.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResult.Pending -> binding.latest.progress.show()
                is ApiResult.Success -> {
                    binding.refresh.isRefreshing = false
                    binding.latest.progress.hide()
                    response.data?.let { update(Upcoming.LATEST, it) }
                }
                is ApiResult.Failure -> {
                    binding.refresh.isRefreshing = false
                    showError(response.exception.message)
                }
            }
        }

        viewModel.pinnedLaunches.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResult.Pending -> binding.pinned.progress.show()
                is ApiResult.Success -> {
                    pinnedAdapter.submitList(response.data)
                    binding.pinned.progress.hide()
                    binding.pinned.pinnedMessage.visibility =
                        if (response.data.isNullOrEmpty()) View.VISIBLE else View.GONE
                }
                is ApiResult.Failure -> showError(response.exception.message)
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

    fun update(dashboardView: DashboardView, response: Launch) {
        with(dashboardView) {
            transitionName = response.id

            launchView.apply {
                patch = response.links?.missionPatch?.patchSmall
                flightNumber = response.flightNumber
                vehicle = response.rocket?.name
                missionName = response.missionName
                date = response.launchDate?.dateUnix?.formatDateMillisLong(response.datePrecision)
            }

            setOnClickListener {
                findNavController().navigate(
                    NavGraphDirections.actionLaunchItemToLaunchDetailsContainer(
                        response.missionName,
                        response.id.orEmpty()
                    ),
                    FragmentNavigatorExtras(dashboardView to response.id.orEmpty())
                )
            }
        }
    }

    private fun setCountdown(launch: Launch, time: Long) {
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(time: Long) {
                binding.next.countdown = String.format(
                    "T-%02d:%02d:%02d:%02d",
                    time.toCountdownDays(),
                    time.toCountdownHours(),
                    time.toCountdownMinutes(),
                    time.toCountdownSeconds()
                )
            }

            override fun onFinish() {
                launch.links?.webcast?.let { link ->
                    binding.next.finish {
                        openWebLink(link)
                    }
                }
            }
        }

        countdownTimer?.start()
    }

    private fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.getLaunches()
    }
}
