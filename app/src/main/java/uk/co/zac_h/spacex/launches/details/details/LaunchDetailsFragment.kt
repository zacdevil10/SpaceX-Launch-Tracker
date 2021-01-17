package uk.co.zac_h.spacex.launches.details.details

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsBinding
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelperImpl
import uk.co.zac_h.spacex.utils.formatDateMillisLong
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchDetailsFragment : Fragment(), LaunchDetailsContract.LaunchDetailsView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var binding: FragmentLaunchDetailsBinding? = null

    private var presenter: LaunchDetailsContract.LaunchDetailsPresenter? = null
    private lateinit var pinnedSharedPreferences: PinnedSharedPreferencesHelper

    private var launch: Launch? = null
    private var id: String? = null

    private var pinned: Boolean = false

    companion object {
        @JvmStatic
        fun newInstance(args: Any) = LaunchDetailsFragment().apply {
            arguments = bundleOf(
                when (args) {
                    is Launch -> "launch_short"
                    is String -> "id"
                    else -> throw IllegalArgumentException()
                } to args
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        launch =
            savedInstanceState?.getParcelable("launch") ?: arguments?.getParcelable("launch_short")
        id = arguments?.getString("id")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        pinnedSharedPreferences = PinnedSharedPreferencesHelperImpl(
            context?.getSharedPreferences(
                "pinned",
                Context.MODE_PRIVATE
            )
        )

        presenter =
            LaunchDetailsPresenterImpl(
                this,
                pinnedSharedPreferences,
                LaunchDetailsInteractorImpl()
            )

        launch?.let {
            presenter?.addLaunchModel(it, true)
            pinned = presenter?.isPinned(it.id) ?: false
        } ?: id?.let { id ->
            pinned = presenter?.isPinned(id) ?: false
            presenter?.get(id)
        }
    }

    override fun onResume() {
        super.onResume()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("launch", launch)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(
            if (launch?.id?.let { presenter?.isPinned(it) } ?: launch?.id?.let {
                    presenter?.isPinned(
                        it
                    )
                } == true) {
                R.menu.menu_details_alternate
            } else {
                R.menu.menu_details
            }, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.pin -> {
            presenter?.pinLaunch(launch?.id ?: id.toString(), true)
            pinned = true
            activity?.invalidateOptionsMenu()
            true
        }
        R.id.unpin -> {
            presenter?.pinLaunch(launch?.id ?: id.toString(), false)
            pinned = false
            activity?.invalidateOptionsMenu()
            true
        }
        R.id.create_event -> {
            presenter?.createEvent()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun updateLaunchDataView(launch: Launch?, isExt: Boolean) {
        launch?.let {
            if (isExt) this.launch = launch

            (activity as MainActivity).supportActionBar?.title = launch.missionName

            binding?.apply {
                Glide.with(this@LaunchDetailsFragment)
                    .load(launch.links?.missionPatch?.patchSmall)
                    .error(context?.let {
                        ContextCompat.getDrawable(it, R.drawable.ic_mission_patch)
                    })
                    .fallback(context?.let {
                        ContextCompat.getDrawable(it, R.drawable.ic_mission_patch)
                    })
                    .placeholder(context?.let {
                        ContextCompat.getDrawable(it, R.drawable.ic_mission_patch)
                    })
                    .into(launchDetailsMissionPatchImage)

                launchDetailsNumberText.text = context?.getString(
                    R.string.flight_number,
                    launch.flightNumber
                )
                launchDetailsRocketTypeText.text = launch.rocket?.name
                launchDetailsMissionNameText.text = launch.missionName

                launchDetailsSiteNameText.text = launch.launchpad?.name

                launchDetailsDateText.text = launch.datePrecision?.let { datePrecision ->
                    launch.launchDate?.dateUnix?.formatDateMillisLong(datePrecision)
                }

                launch.staticFireDate?.dateUnix?.let { date ->
                    launchDetailsStaticFireDateLabel.visibility = View.VISIBLE
                    launchDetailsStaticFireDateText.visibility = View.VISIBLE
                    launchDetailsStaticFireDateText.text = date.formatDateMillisLong()
                }

                launchDetailsDetailsText.visibility =
                    if (launch.details.isNullOrEmpty()) View.GONE else View.VISIBLE
                launchDetailsDetailsText.text = launch.details

                launch.links?.webcast?.let { link ->
                    launchDetailsWatchButton.visibility = View.VISIBLE
                    launchDetailsWatchButton.setOnClickListener {
                        openWebLink(link)
                    }
                } ?: run {
                    launchDetailsWatchButton.visibility = View.GONE
                }

                launch.links?.presskit?.let { link ->
                    launchDetailsPressKitButton.visibility = View.VISIBLE
                    launchDetailsPressKitButton.setOnClickListener {
                        openWebLink(link)
                    }
                } ?: run {
                    launchDetailsPressKitButton.visibility = View.GONE
                }

                launch.links?.wikipedia?.let { link ->
                    launchDetailsWikiButton.visibility = View.VISIBLE
                    launchDetailsWikiButton.setOnClickListener {
                        launchDetailsWikiButton.visibility = View.VISIBLE
                        openWebLink(link)
                    }
                } ?: run {
                    launchDetailsWikiButton.visibility = View.GONE
                }

                launch.links?.redditLinks?.campaign?.let { link ->
                    launchDetailsCampaignButton.visibility = View.VISIBLE
                    launchDetailsCampaignButton.setOnClickListener {
                        openWebLink(link)
                    }
                } ?: run {
                    launchDetailsCampaignButton.visibility = View.GONE
                }

                launch.links?.redditLinks?.launch?.let { link ->
                    launchDetailsLaunchButton.visibility = View.VISIBLE
                    launchDetailsLaunchButton.setOnClickListener {
                        openWebLink(link)
                    }
                } ?: run {
                    launchDetailsLaunchButton.visibility = View.GONE
                }

                launch.links?.redditLinks?.media?.let { link ->
                    launchDetailsMediaButton.visibility = View.VISIBLE
                    launchDetailsMediaButton.setOnClickListener {
                        openWebLink(link)
                    }
                } ?: run {
                    launchDetailsMediaButton.visibility = View.GONE
                }
            }
        }
    }

    override fun newCalendarEvent() {
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

    override fun openWebLink(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    override fun showProgress() {
        binding?.progressIndicator?.show()
    }

    override fun hideProgress() {
        binding?.progressIndicator?.hide()
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (launch == null) launch?.id?.let {
                presenter?.get(it)
            }
        }
    }
}
