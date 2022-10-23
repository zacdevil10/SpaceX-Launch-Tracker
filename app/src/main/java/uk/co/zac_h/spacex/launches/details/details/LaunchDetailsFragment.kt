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
import com.google.android.material.button.MaterialButton
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

        viewModel.launch?.let { update(it) }
    }

    private fun update(response: LaunchItem) {
        with(binding) {
            launchView.apply {
                patch = response.missionPatch
                vehicle = response.rocket
                missionName = response.missionName
                date = response.launchDate

                if (response.rocket == "Falcon 9") {
                    isReused = response.firstStage?.firstOrNull()?.reused ?: false
                    landingPad = response.firstStage?.firstOrNull()?.landingLocation
                } else {
                    isReused = false
                    landingPad = null
                }
            }

            launchDetailsSiteNameText.text = response.launchLocation

            launchDetailsDetailsText.apply {
                isVisible = !response.description.isNullOrEmpty()
                text = response.description
            }

            launchDetailsWatchButton.setLink(response.webcast)

            if (response.upcoming) {
                launchDetailsCalendarButton.visibility = View.VISIBLE
                launchDetailsCalendarButton.setOnClickListener {
                    createEvent(response)
                }
            } else launchDetailsCalendarButton.visibility = View.GONE
        }
    }

    private fun MaterialButton.setLink(link: String?) {
        link?.let {
            visibility = View.VISIBLE
            setOnClickListener { openWebLink(link) }
        } ?: run { visibility = View.GONE }
    }

    private fun createEvent(launch: LaunchItem) {
        val calendarIntent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(
                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                launch.launchDate
            )
            putExtra(
                CalendarContract.EXTRA_EVENT_END_TIME,
                launch.launchDate
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

