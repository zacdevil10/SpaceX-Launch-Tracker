package uk.co.zac_h.spacex.launches.details.details

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsBinding
import uk.co.zac_h.spacex.model.spacex.DatePrecision
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelperImpl
import uk.co.zac_h.spacex.utils.formatDateMillisLong

class LaunchDetailsFragment : BaseFragment(), LaunchDetailsContract.LaunchDetailsView {

    override var title: String = "Launch Details"

    private var binding: FragmentLaunchDetailsBinding? = null

    private var presenter: LaunchDetailsContract.LaunchDetailsPresenter? = null
    private lateinit var pinnedSharedPreferences: PinnedSharedPreferencesHelper

    private var launch: Launch? = null
    private var id: String? = null

    private var pinned: Boolean = false

    companion object {
        const val LAUNCH_KEY = "launch"
        const val LAUNCH_KEY_SHORT = "launch_short"
        const val ID_KEY = "id"

        @JvmStatic
        fun newInstance(args: Any) = LaunchDetailsFragment().apply {
            arguments = bundleOf(
                when (args) {
                    is Launch -> LAUNCH_KEY_SHORT
                    is String -> ID_KEY
                    else -> throw IllegalArgumentException()
                } to args
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        launch =
            savedInstanceState?.getParcelable(LAUNCH_KEY) ?: arguments?.getParcelable(LAUNCH_KEY_SHORT)
        id = arguments?.getString(ID_KEY)
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(LAUNCH_KEY, launch)
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
        else -> super.onOptionsItemSelected(item)
    }

    override fun update(data: Any, response: Launch?) {
        launch?.let {
            if (data as Boolean) this.launch = response

            (activity as MainActivity).supportActionBar?.title = it.missionName

            binding?.apply {
                Glide.with(this@LaunchDetailsFragment)
                    .load(it.links?.missionPatch?.patchSmall)
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
                    it.flightNumber
                )
                launchDetailsRocketTypeText.text = it.rocket?.name
                launchDetailsMissionNameText.text = it.missionName

                launchDetailsSiteNameText.text = it.launchpad?.name

                launchDetailsDateText.text = it.datePrecision?.let { datePrecision ->
                    it.launchDate?.dateUnix?.formatDateMillisLong(datePrecision)
                }

                it.staticFireDate?.dateUnix?.let { date ->
                    launchDetailsStaticFireDateLabel.visibility = View.VISIBLE
                    launchDetailsStaticFireDateText.visibility = View.VISIBLE
                    launchDetailsStaticFireDateText.text = date.formatDateMillisLong()
                }

                launchDetailsDetailsText.visibility =
                    if (it.details.isNullOrEmpty()) View.GONE else View.VISIBLE
                launchDetailsDetailsText.text = it.details

                it.links?.webcast?.let { link ->
                    launchDetailsWatchButton.visibility = View.VISIBLE
                    launchDetailsCalendarButton.visibility = View.GONE
                    launchDetailsWatchButton.setOnClickListener {
                        openWebLink(link)
                    }
                } ?: run {
                    launchDetailsWatchButton.visibility = View.GONE
                }

                if (it.datePrecision == DatePrecision.DAY || it.datePrecision == DatePrecision.HOUR) {
                    launchDetailsCalendarButton.setOnClickListener {
                        presenter?.createEvent()
                    }
                } else {
                    launchDetailsCalendarButton.visibility = View.GONE
                }

                it.links?.presskit?.let { link ->
                    launchDetailsPressKitButton.visibility = View.VISIBLE
                    launchDetailsPressKitButton.setOnClickListener {
                        openWebLink(link)
                    }
                } ?: run {
                    launchDetailsPressKitButton.visibility = View.GONE
                }

                it.links?.wikipedia?.let { link ->
                    launchDetailsWikiButton.visibility = View.VISIBLE
                    launchDetailsWikiButton.setOnClickListener {
                        launchDetailsWikiButton.visibility = View.VISIBLE
                        openWebLink(link)
                    }
                } ?: run {
                    launchDetailsWikiButton.visibility = View.GONE
                }

                it.links?.redditLinks?.campaign?.let { link ->
                    launchDetailsCampaignButton.visibility = View.VISIBLE
                    launchDetailsCampaignButton.setOnClickListener {
                        openWebLink(link)
                    }
                } ?: run {
                    launchDetailsCampaignButton.visibility = View.GONE
                }

                it.links?.redditLinks?.launch?.let { link ->
                    launchDetailsLaunchButton.visibility = View.VISIBLE
                    launchDetailsLaunchButton.setOnClickListener {
                        openWebLink(link)
                    }
                } ?: run {
                    launchDetailsLaunchButton.visibility = View.GONE
                }

                it.links?.redditLinks?.media?.let { link ->
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
        binding?.progress?.show()
    }

    override fun hideProgress() {
        binding?.progress?.hide()
    }

    override fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (launch == null) launch?.id?.let {
                presenter?.get(it)
            }
        }
    }
}
