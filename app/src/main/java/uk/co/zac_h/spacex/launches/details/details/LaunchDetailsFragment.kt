package uk.co.zac_h.spacex.launches.details.details

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsBinding
import uk.co.zac_h.spacex.dto.spacex.DatePrecision
import uk.co.zac_h.spacex.dto.spacex.Launch
import uk.co.zac_h.spacex.utils.*

class LaunchDetailsFragment : BaseFragment(), NetworkInterface.View<Launch> {

    private lateinit var binding: FragmentLaunchDetailsBinding

    private var presenter: LaunchDetailsContract.LaunchDetailsPresenter? = null
    private lateinit var pinnedSharedPreferences: PinnedSharedPreferencesHelper

    private var launch: Launch? = null
    private lateinit var id: String

    companion object {
        @JvmStatic
        fun newInstance(args: Any) = LaunchDetailsFragment().apply {
            when (args) {
                is Launch -> {
                    launch = args
                    id = args.id
                }
                is String -> id = args
                else -> throw IllegalArgumentException("${this.javaClass.simpleName} has been created with an invalid argument type")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        savedInstanceState?.let {
            id = it.getString("id").orUnknown()
            //launch = it.getParcelable("launch")
        }
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

        pinnedSharedPreferences = PinnedSharedPreferencesHelperImpl(
            requireContext().getSharedPreferences("pinned", Context.MODE_PRIVATE)
        )

        presenter = LaunchDetailsPresenterImpl(
            this,
            pinnedSharedPreferences,
            LaunchDetailsInteractorImpl()
        )

        binding.swipeRefresh.setOnRefreshListener {

            presenter?.get(id)
        }

        presenter?.getOrUpdate(launch, id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("id", id)
        //outState.putParcelable("launch", launch)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(
            if (presenter?.isPinned(id) == true) R.menu.menu_details_alternate else R.menu.menu_details,
            menu
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.pin -> {
            presenter?.pinLaunch(id, true)
            activity?.invalidateOptionsMenu()
            true
        }
        R.id.unpin -> {
            presenter?.pinLaunch(id, false)
            activity?.invalidateOptionsMenu()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun update(response: Launch) {
        //

        launch = response

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
                    createEvent()
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

    private fun createEvent() {
        launch?.let {
            val calendarIntent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(
                    CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                    it.launchDate?.dateUnix?.times(1000L)
                )
                putExtra(
                    CalendarContract.EXTRA_EVENT_END_TIME,
                    it.launchDate?.dateUnix?.times(1000L)?.plus(3600000)
                )
                putExtra(
                    CalendarContract.Events.TITLE,
                    "${it.missionName} - SpaceX"
                )
            }
            try {
                startActivity(calendarIntent)
            } catch (e: ActivityNotFoundException) {
                showError("No supported calendar apps found.")
            }
        }
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    override fun showProgress() {
        binding.progress.show()
    }

    override fun hideProgress() {
        binding.progress.hide()
    }

    override fun showError(error: String) {

        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        /**/
    }
}
