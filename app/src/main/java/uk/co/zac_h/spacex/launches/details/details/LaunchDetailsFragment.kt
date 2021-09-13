package uk.co.zac_h.spacex.launches.details.details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsBinding
import uk.co.zac_h.spacex.dto.spacex.DatePrecision
import uk.co.zac_h.spacex.dto.spacex.Launch
import uk.co.zac_h.spacex.launches.details.LaunchDetailsContainerViewModel
import uk.co.zac_h.spacex.utils.*

class LaunchDetailsFragment : BaseFragment() {

    private val viewModel: LaunchDetailsContainerViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentLaunchDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

        viewModel.launch.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                ApiResult.Status.PENDING -> {
                }
                ApiResult.Status.SUCCESS -> response.data?.let { update(it) }.also {
                    binding.swipeRefresh.isRefreshing = false
                }
                ApiResult.Status.FAILURE -> binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(
            if (viewModel.isPinned()) R.menu.menu_details_alternate else R.menu.menu_details,
            menu
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.pin -> {
            viewModel.pinLaunch(true)
            activity?.invalidateOptionsMenu()
            true
        }
        R.id.unpin -> {
            viewModel.pinLaunch(false)
            activity?.invalidateOptionsMenu()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun update(response: Launch) {
        Glide.with(this@LaunchDetailsFragment)
            .load(response.links?.missionPatch?.patchSmall)
            .error(ContextCompat.getDrawable(requireContext(), R.drawable.ic_mission_patch))
            .fallback(ContextCompat.getDrawable(requireContext(), R.drawable.ic_mission_patch))
            .placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.ic_mission_patch))
            .into(binding.launchDetailsMissionPatchImage)

        with(binding) {
            launchDetailsNumberText.text = getString(
                R.string.flight_number,
                response.flightNumber
            )
            launchDetailsRocketTypeText.text = response.rocket?.name
            launchDetailsMissionNameText.text = response.missionName

            launchDetailsSiteNameText.text = response.launchpad?.name

            launchDetailsDateText.text = response.datePrecision?.let { datePrecision ->
                response.launchDate?.dateUnix?.formatDateMillisLong(/*datePrecision*/)
            }

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
