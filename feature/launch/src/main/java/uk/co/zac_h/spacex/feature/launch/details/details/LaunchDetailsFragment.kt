package uk.co.zac_h.spacex.feature.launch.details.details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.fragment.openWebLink
import uk.co.zac_h.spacex.core.common.recyclerview.HorizontalMarginItemDecoration
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.feature.launch.LaunchItem
import uk.co.zac_h.spacex.feature.launch.LaunchesViewModel
import uk.co.zac_h.spacex.feature.launch.R
import uk.co.zac_h.spacex.feature.launch.adapters.VideoAdapter
import uk.co.zac_h.spacex.feature.launch.databinding.FragmentLaunchDetailsBinding

class LaunchDetailsFragment : BaseFragment(), ViewPagerFragment {

    override val title: String = "Details"

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.launch_nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentLaunchDetailsBinding

    private lateinit var videoAdapter: VideoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoAdapter = VideoAdapter { openWebLink(it) }

        viewModel.launch?.let { launch ->
            with(binding) {
                launchView.apply {
                    with(launch.countdown(root.resources)) {
                        countdown.isVisible = this != null
                        countdown.countdown = { launch.countdown(resources) }
                        if (this != null) countdown.startTimer()
                    }

                    patch = launch.missionPatch
                    vehicle = launch.rocket
                    missionName = launch.missionName
                    date = launch.launchDate

                    if (launch.rocket == "Falcon 9") {
                        isReused = launch.firstStage?.firstOrNull()?.reused ?: false
                        landingPad = launch.firstStage?.firstOrNull()?.landingLocation?.let {
                            if (it == "N/A") null else it
                        }
                    } else {
                        isReused = false
                        landingPad = null
                    }
                }

                holdCard.isVisible = !launch.holdReason.isNullOrEmpty()
                holdDescription.text = launch.holdReason

                missionCard.isVisible = !launch.description.isNullOrEmpty()
                        || !launch.type.isNullOrEmpty()
                        || !launch.orbit.isNullOrEmpty()
                missionDescription.apply {
                    isVisible = !launch.description.isNullOrEmpty()
                    text = launch.description
                }
                type.text = launch.type
                orbit.text = launch.orbit

                launchDetailsVideoList.apply {
                    layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    setHasFixedSize(false)
                    adapter = videoAdapter
                    addItemDecoration(
                        HorizontalMarginItemDecoration(
                            resources.getDimensionPixelSize(R.dimen.small_margin)
                        )
                    )
                    isVisible = !launch.webcast.isNullOrEmpty()
                }

                videoAdapter.submitList(launch.webcast)

                launchDetailsCalendarButton.apply {
                    isVisible = launch.upcoming && !launch.webcastLive
                    setOnClickListener { createEvent(launch) }
                }

                statusCard.isVisible = launch.status.type != null
                status.text = launch.status.name
                statusDescription.text =
                    if (launch.status.type == 4) launch.failReason else launch.status.description

                launchSiteCard.isVisible = launch.launchLocation != null
                launchSite.text = launch.launchLocation
                Glide.with(requireContext()).load(launch.launchLocationMap).into(launchSiteImage)
                launchSiteImage.setOnClickListener {
                    launch.launchLocationMapUrl?.let { launchLocationMapUrl ->
                        openWebLink(launchLocationMapUrl)
                    }
                }
            }
        }
    }

    private fun createEvent(launch: LaunchItem) {
        val calendarIntent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(
                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                launch.launchDateUnix
            )
            putExtra(
                CalendarContract.EXTRA_EVENT_END_TIME,
                launch.launchDateUnix
            )
            putExtra(
                CalendarContract.Events.TITLE,
                "${launch.missionName} - SpaceX"
            )
        }
        try {
            startActivity(calendarIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No supported calendar apps found.", Toast.LENGTH_SHORT).show()
        }
    }
}

