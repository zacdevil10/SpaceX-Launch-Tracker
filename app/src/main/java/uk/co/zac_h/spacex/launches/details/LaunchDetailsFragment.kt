package uk.co.zac_h.spacex.launches.details

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.CalendarContract
import android.view.*
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.google.android.material.transition.MaterialContainerTransform
import com.squareup.picasso.Picasso
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsBinding
import uk.co.zac_h.spacex.launches.adapters.FirstStageAdapter
import uk.co.zac_h.spacex.launches.adapters.LaunchLinksAdapter
import uk.co.zac_h.spacex.launches.adapters.PayloadAdapter
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelperImpl
import uk.co.zac_h.spacex.utils.formatDateMillisLong
import uk.co.zac_h.spacex.utils.models.LinksModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchDetailsFragment : Fragment(), LaunchDetailsContract.LaunchDetailsView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentLaunchDetailsBinding? = null
    private val binding get() = _binding!!

    private var presenter: LaunchDetailsContract.LaunchDetailsPresenter? = null
    private lateinit var pinnedSharedPreferences: PinnedSharedPreferencesHelper

    private var launch: LaunchesModel? = null
    private var id: String? = null

    private var pinned: Boolean = false

    private var coreAssigned: Boolean = false

    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        postponeEnterTransition()

        sharedElementEnterTransition = MaterialContainerTransform()

        launch = if (savedInstanceState != null) {
            savedInstanceState.getParcelable("launch")
        } else {
            arguments?.getParcelable("launch") as LaunchesModel?
        }
        id = arguments?.getString("launch_id")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaunchDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pinnedSharedPreferences = PinnedSharedPreferencesHelperImpl(
            context?.getSharedPreferences(
                "pinned",
                Context.MODE_PRIVATE
            )
        )

        presenter = LaunchDetailsPresenterImpl(
            this,
            pinnedSharedPreferences,
            LaunchDetailsInteractorImpl()
        )

        launch?.let {
            presenter?.addLaunchModel(it)
            binding.launchDetailsContainer.transitionName = it.flightNumber.toString()
            pinned = presenter?.isPinned(it.flightNumber.toString()) ?: false
        } ?: id?.let {
            presenter?.getLaunch(it)
            pinned = presenter?.isPinned(it) ?: false
        }

        binding.launchDetailsFirstStageText.setOnClickListener {
            expandCollapse(
                binding.launchDetailsCoresRecycler,
                binding.launchDetailsFirstStageCollapseToggle
            )
        }

        binding.launchDetailsPayloadText.setOnClickListener {
            expandCollapse(
                binding.launchDetailsPayloadRecycler,
                binding.launchDetailsPayloadCollapseToggle
            )
        }

        //Set rotation animation of toggle buttons when expanding/collapsing recycler view
        val rotation = RotateAnimation(
            180f,
            0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        ).apply {
            duration = 500
        }

        binding.launchDetailsFirstStageCollapseToggle.setOnCheckedChangeListener { compoundButton, _ ->
            compoundButton.startAnimation(rotation)
        }

        binding.launchDetailsPayloadCollapseToggle.setOnCheckedChangeListener { compoundButton, _ ->
            compoundButton.startAnimation(rotation)
        }

        startPostponedEnterTransition()
    }

    override fun onResume() {
        super.onResume()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
        /**
         * Set and restore Expand/Collapse state of recycler view when returning to fragment
         */
        setupExpandCollapse(
            binding.launchDetailsCoresRecycler,
            binding.launchDetailsFirstStageCollapseToggle
        )
        setupExpandCollapse(
            binding.launchDetailsPayloadRecycler,
            binding.launchDetailsPayloadCollapseToggle
        )
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
        countdownTimer?.cancel()
        countdownTimer = null
        presenter?.cancelRequest()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details, menu)
        menu.findItem(R.id.pin).icon = context?.let {
            ContextCompat.getDrawable(
                it,
                if (id?.let { id -> presenter?.isPinned(id) } ?: presenter?.isPinned(launch?.flightNumber.toString()) == true) {
                    R.drawable.ic_star_black_24dp
                } else {
                    R.drawable.ic_star_border_black_24dp
                })
        }

        setIconTint(menu.findItem(R.id.pin))
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.pin -> {
            presenter?.pinLaunch(launch?.flightNumber?.toString() ?: id.toString(), !pinned)

            pinned = !pinned

            item.icon = context?.let {
                ContextCompat.getDrawable(
                    it,
                    if (pinned) R.drawable.ic_star_black_24dp else R.drawable.ic_star_border_black_24dp
                )
            }

            setIconTint(item)
            true
        }
        R.id.create_event -> {
            presenter?.createEvent()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setIconTint(item: MenuItem) {
        var drawable = item.icon
        drawable = DrawableCompat.wrap(drawable)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DrawableCompat.setTint(
                drawable.mutate(),
                resources.getColor(android.R.color.white, null)
            )
        } else {
            @Suppress("DEPRECATION")
            DrawableCompat.setTint(drawable.mutate(), resources.getColor(android.R.color.white))
        }
        item.icon = drawable
    }

    override fun updateLaunchDataView(launch: LaunchesModel?) {
        launch?.let {
            this.launch = launch

            binding.apply {
                launchDetailsContainer.transitionName = it.flightNumber.toString()

                it.tbd?.let { tbd ->
                    val time = launch.launchDateUnix.times(1000) - System.currentTimeMillis()
                    if (!tbd && time >= 0) {
                        setCountdown(time)
                        showCountdown()
                    } else {
                        hideCountdown()
                    }
                }

                if (id == null) id = launch.flightNumber.toString()

                launchDetailsMissionPatchImage.visibility =
                    launch.links.missionPatchSmall?.let { View.VISIBLE } ?: View.GONE

                Picasso.get().load(launch.links.missionPatchSmall)
                    .into(launchDetailsMissionPatchImage)

                launchDetailsNumberText.text = context?.getString(
                    R.string.flight_number,
                    launch.flightNumber
                )
                launchDetailsRocketTypeText.text = launch.rocket.name
                launchDetailsMissionNameText.text = launch.missionName

                launchDetailsSiteNameText.text = launch.launchSite.name

                launchDetailsDateText.text = launch.tbd?.let { tbd ->
                    launch.launchDateUnix.formatDateMillisLong(tbd)
                } ?: launch.launchDateUnix.formatDateMillisLong()

                launch.staticFireDateUnix?.let { date ->
                    launchDetailsStaticFireDateLabel.visibility = View.VISIBLE
                    launchDetailsStaticFireDateText.text = date.formatDateMillisLong()
                } ?: run {
                    launchDetailsStaticFireDateText.visibility = View.GONE
                }

                launchDetailsDetailsText.visibility =
                    if (launch.details.isNullOrEmpty()) View.GONE else View.VISIBLE
                launchDetailsDetailsText.text = launch.details

                launch.rocket.firstStage?.cores?.forEach { core ->
                    if (coreAssigned) return@forEach
                    coreAssigned = core.serial != null
                }

                if (coreAssigned) launch.rocket.firstStage?.cores?.let {
                    //If a core has been assigned to a launch then add the adapter to the RecyclerView
                    launchDetailsCoresRecycler.apply {
                        layoutManager = LinearLayoutManager(this@LaunchDetailsFragment.context)
                        setHasFixedSize(true)
                        adapter = FirstStageAdapter(it)
                    }
                } else {
                    //If no core has been assigned yet then hide the RecyclerView and related heading
                    launchDetailsFirstStageText.visibility = View.GONE
                    launchDetailsFirstStageCollapseToggle.visibility = View.GONE
                }

                launchDetailsPayloadRecycler.apply {
                    layoutManager = LinearLayoutManager(this@LaunchDetailsFragment.context)
                    setHasFixedSize(false)
                    addItemDecoration(
                        DividerItemDecoration(
                            this.context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    adapter = PayloadAdapter(
                        this@LaunchDetailsFragment.context,
                        launch.rocket.secondStage?.payloads
                    )
                    (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                }

                val links = ArrayList<LinksModel>()

                launch.links.videoLink?.let { link ->
                    links.add(
                        LinksModel(
                            "Watch",
                            link
                        )
                    )
                }
                launch.links.redditCampaign?.let { link ->
                    links.add(
                        LinksModel(
                            "Reddit Campaign",
                            link
                        )
                    )
                }
                launch.links.redditLaunch?.let { link ->
                    links.add(
                        LinksModel(
                            "Reddit Launch",
                            link
                        )
                    )
                }
                launch.links.redditMedia?.let { link ->
                    links.add(
                        LinksModel(
                            "Reddit Media",
                            link
                        )
                    )
                }
                launch.links.presskit?.let { link ->
                    links.add(
                        LinksModel(
                            "Press Kit",
                            link
                        )
                    )
                }
                launch.links.wikipedia?.let { link ->
                    links.add(
                        LinksModel(
                            "Wikipedia Article",
                            link
                        )
                    )
                }

                if (links.isEmpty()) launchDetailsLinksText.visibility = View.GONE

                launchDetailsLinksRecycler.apply {
                    layoutManager = GridLayoutManager(this@LaunchDetailsFragment.context, 3)
                    setHasFixedSize(true)
                    adapter = LaunchLinksAdapter(links, this@LaunchDetailsFragment)
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
                    it.launchDateUnix.times(1000L)
                )
                putExtra(
                    CalendarContract.EXTRA_EVENT_END_TIME,
                    it.launchDateUnix.times(1000L).plus(3600000)
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

    /**
     * Toggle expand/collapse of [recycler] views on selection of TextView
     *
     * @param[recycler] is the view you wish to show or hide
     * @param[expCollapse] is the toggle button that shows the collapse state of the recycler
     */
    private fun expandCollapse(recycler: RecyclerView, expCollapse: ToggleButton) {
        when {
            expCollapse.isChecked -> recycler.visibility = View.GONE
            else -> recycler.visibility = View.VISIBLE
        }
        expCollapse.isChecked = !expCollapse.isChecked
    }

    /**
     * Initial expand/collapse of [recycler] views
     *
     * @param[recycler] is the view you wish to show or hide
     * @param[expCollapse] is the toggle button that shows the collapse state of the recycler
     */
    private fun setupExpandCollapse(recycler: RecyclerView, expCollapse: ToggleButton) {
        if (expCollapse.isChecked) {
            expCollapse.isChecked = true
            recycler.visibility = View.VISIBLE
        } else {
            expCollapse.isChecked = false
            recycler.visibility = View.GONE
        }
    }

    override fun openWebLink(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    override fun setCountdown(time: Long) {
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                presenter?.updateCountdown(millisUntilFinished)
            }

            override fun onFinish() {

            }
        }

        countdownTimer?.start()
    }

    override fun updateCountdown(countdown: String) {
        binding.launchDetailsCountdownText.text = countdown
    }

    override fun showProgress() {
        binding.launchDetailsProgress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.launchDetailsProgress.visibility = View.GONE
    }

    override fun showCountdown() {
        binding.launchDetailsCountdownText.visibility = View.VISIBLE
    }

    override fun hideCountdown() {
        binding.launchDetailsCountdownText.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            id?.let {
                if (launch == null) presenter?.getLaunch(it)
            }
        }
    }
}
