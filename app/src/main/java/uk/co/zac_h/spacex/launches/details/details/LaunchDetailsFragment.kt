package uk.co.zac_h.spacex.launches.details.details

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
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsBinding
import uk.co.zac_h.spacex.launches.LaunchItem
import uk.co.zac_h.spacex.launches.LaunchesViewModel
import uk.co.zac_h.spacex.utils.openWebLink

class LaunchDetailsFragment : BaseFragment() {

    override val title: String = "Details"

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentLaunchDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                        landingPad = launch.firstStage?.firstOrNull()?.landingLocation
                    } else {
                        isReused = false
                        landingPad = null
                    }
                }

                missionDescription.apply {
                    isVisible = !launch.description.isNullOrEmpty()
                    text = launch.description
                }
                typeText.text = launch.type
                orbitText.text = launch.orbit

                launchSiteCard.isVisible = launch.launchLocation != null
                launchSiteText.text = launch.launchLocation
                Glide.with(requireContext()).load(launch.launchLocationMap).into(launchSiteImage)
                launchSiteImage.setOnClickListener { openWebLink(launch.launchLocationMapUrl) }

                statusCard.isVisible = launch.status != null && launch.statusDescription != null
                statusText.text = launch.status
                statusDescription.text = launch.statusDescription

                launchDetailsWatchButton.apply {
                    isVisible = launch.webcast != null
                    setOnClickListener { openWebLink(launch.webcast) }
                }

                launchDetailsCalendarButton.apply {
                    isVisible = launch.upcoming
                    setOnClickListener { createEvent(launch) }
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

