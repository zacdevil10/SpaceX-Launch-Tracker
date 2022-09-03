package uk.co.zac_h.spacex.launches.details.details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.navGraphViewModels
import com.google.android.material.button.MaterialButton
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsBinding
import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.launches.details.LaunchDetailsContainerViewModel
import uk.co.zac_h.spacex.types.DatePrecision
import uk.co.zac_h.spacex.utils.formatDateMillisLong
import uk.co.zac_h.spacex.utils.openWebLink

class LaunchDetailsFragment : BaseFragment() {

    private val viewModel: LaunchDetailsContainerViewModel by navGraphViewModels(R.id.nav_graph) {
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

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getLaunch(CachePolicy.REFRESH)
        }

        viewModel.launch.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Pending -> {}
                is ApiResult.Success -> result.data?.let { update(it) }.also {
                    binding.swipeRefresh.isRefreshing = false
                }
                is ApiResult.Failure -> binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun update(response: Launch) {
        with(binding) {
            launchView.apply {
                patch = response.links?.missionPatch?.patchSmall
                flightNumber = response.flightNumber
                vehicle = response.rocket?.name
                missionName = response.missionName
                date = response.datePrecision?.let { datePrecision ->
                    response.launchDate?.dateUnix?.formatDateMillisLong(datePrecision)
                }
            }

            launchDetailsSiteNameText.text = response.launchpad?.name

            response.staticFireDate?.dateUnix?.let { date ->
                launchDetailsStaticFireDateLabel.visibility = View.VISIBLE
                launchDetailsStaticFireDateText.visibility = View.VISIBLE
                launchDetailsStaticFireDateText.text = date.formatDateMillisLong()
            }

            launchDetailsDetailsText.visibility =
                if (response.details.isNullOrEmpty()) View.GONE else View.VISIBLE
            launchDetailsDetailsText.text = response.details

            launchDetailsWatchButton.setLink(response.links?.webcast)

            if (response.datePrecision == DatePrecision.DAY ||
                response.datePrecision == DatePrecision.HOUR &&
                response.upcoming == true
            ) {
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
