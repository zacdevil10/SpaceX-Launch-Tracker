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
import uk.co.zac_h.spacex.core.utils.formatDate
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsBinding
import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.launches.LaunchesViewModel
import uk.co.zac_h.spacex.utils.openWebLink

class LaunchDetailsFragment : BaseFragment() {

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

    private fun update(response: Launch) {
        with(binding) {
            launchView.apply {
                patch = response.links?.missionPatch?.patchSmall
                vehicle = response.rocket?.name
                missionName = response.missionName
                date = response.launchDate?.dateUtc?.formatDate()

                if (response.rocket?.name == "Falcon 9") {
                    isReused = response.cores?.firstOrNull()?.reused ?: false
                    landingPad = response.cores?.firstOrNull()?.landingPad?.name
                } else {
                    isReused = false
                    landingPad = null
                }
            }

            launchDetailsSiteNameText.text = response.launchpad?.name

            launchDetailsDetailsText.apply {
                isVisible = !response.details.isNullOrEmpty()
                text = response.details
            }

            launchDetailsWatchButton.setLink(response.links?.webcast)

            if (response.upcoming == true) {
                launchDetailsCalendarButton.visibility = View.VISIBLE
                launchDetailsCalendarButton.setOnClickListener {
                    createEvent(response)
                }
            } else launchDetailsCalendarButton.visibility = View.GONE

            launchDetailsWikiButton.setLink(response.links?.wikipedia)
            launchDetailsCampaignButton.setLink(response.links?.redditLinks?.campaign)
            launchDetailsLaunchButton.setLink(response.links?.redditLinks?.launch)
            launchDetailsMediaButton.setLink(response.links?.redditLinks?.media)
        }
    }

    private fun MaterialButton.setLink(link: String?) {
        link?.let {
            visibility = View.VISIBLE
            setOnClickListener { openWebLink(link) }
        } ?: run { visibility = View.GONE }
    }

    private fun createEvent(launch: Launch) {
        val calendarIntent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(
                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                launch.launchDate?.dateUnix?.times(1000L)
            )
            putExtra(
                CalendarContract.EXTRA_EVENT_END_TIME,
                launch.launchDate?.dateUnix?.times(1000L)?.plus(3600000)
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

