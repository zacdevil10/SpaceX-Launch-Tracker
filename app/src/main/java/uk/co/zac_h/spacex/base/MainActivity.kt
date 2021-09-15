package uk.co.zac_h.spacex.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ActivityMainBinding
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    OnNetworkStateChangeListener.NetworkStateReceiverListener,
    NavController.OnDestinationChangedListener {

    private val startDestinations = mutableSetOf(
        R.id.dashboard_page_fragment,
        R.id.news_page_fragment,
        R.id.launches_page_fragment,
        R.id.crew_page_fragment,
        R.id.vehicles_page_fragment,
        R.id.statistics_page_fragment
    )

    private lateinit var binding: ActivityMainBinding

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).preferencesRepo.themeModeLive.observe(this, { mode ->
            mode?.let { delegate.localNightMode = it }
        })

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let {
            currentPosition = it.getInt(CREW_POSITION_KEY)
        }

        (application as App).networkStateChangeListener.apply {
            addListener(this@MainActivity)
            registerReceiver()
        }

        snackbar = Snackbar.make(
            binding.drawerLayout,
            R.string.network_connection,
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(R.string.dismiss_label) { dismiss() }
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment

        navHostFragment.navController.addOnDestinationChangedListener(this)

        binding.navView.setupWithNavController(navHostFragment.navController)

        val appBarConfig = AppBarConfiguration.Builder(startDestinations)
            .setOpenableLayout(binding.drawerLayout).build()

        setSupportActionBar(binding.bottomAppBar)
        binding.bottomAppBar.setupWithNavController(navHostFragment.navController, appBarConfig)
    }

    override fun onStart() {
        super.onStart()
        (application as App).networkStateChangeListener.updateState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CREW_POSITION_KEY, currentPosition)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        snackbar = null
        (application as App).networkStateChangeListener.apply {
            removeListener(this@MainActivity)
            unregisterReceiver()
        }
    }

    override fun networkAvailable() {
        snackbar?.dismiss()
    }

    override fun networkLost() {
        snackbar?.show()
    }

    companion object {
        const val CREW_POSITION_KEY = "crew_pager_position"
        var currentPosition: Int = 0
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            //Launches
            R.id.dashboard_page_fragment -> setAppBarForDashboard()
            R.id.launches_page_fragment -> setAppBarForLaunches()
            R.id.launch_details_container_fragment -> setAppBarForLaunchDetails()
            //News
            R.id.news_page_fragment -> setAppBarForNews()
            //Crew
            R.id.crew_page_fragment -> setAppBarForCrew()
            R.id.crew_detail_page_fragment -> setAppBarForCrewDetails()
            //Statistics
            R.id.statistics_page_fragment -> setAppBarForStatistics(false)
            R.id.launch_history_fragment -> setAppBarForStatistics(true)
            R.id.landing_history_fragment -> setAppBarForStatistics(false)
            R.id.launch_rate_fragment -> setAppBarForStatistics(false)
            R.id.launch_mass_fragment -> setAppBarForStatistics(true)
            R.id.fairing_recovery_fragment -> setAppBarForStatistics(false)
            R.id.pad_stats_fragment -> setAppBarForStatistics(false)
            //Vehicles
            R.id.vehicles_page_fragment -> setAppBarForVehicles()
            R.id.rocket_details_fragment -> setAppBarForRocketDetails()
            R.id.dragon_details_fragment -> setAppBarForDragonDetails()
            R.id.ship_details_fragment -> setAppBarForShipDetails()
            R.id.core_details_fragment -> setAppBarForCoreDetails()
            R.id.capsule_details_fragment -> setAppBarForCapsuleDetails()
            //About
            R.id.company_page_fragment -> setAppBarForCompany()
            R.id.history_page_fragment -> setAppBarForHistory()
            R.id.about_page_fragment -> setAppBarForAbout()
            //Settings dialogs
            //R.id.theme_alert_dialog,
            //R.id.dashboard_edit_dialog -> showAppBar()
        }
    }

    private fun setAppBarForDashboard() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
        }
    }

    private fun setAppBarForLaunches() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.show()
        }
    }

    private fun setAppBarForLaunchDetails() {
        hideAppBar()
        binding.fab.hide()
    }

    private fun setAppBarForNews() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
        }
    }

    private fun setAppBarForCrew() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
        }
    }

    private fun setAppBarForCrewDetails() {
        hideAppBar()
        binding.fab.hide()
    }

    private fun setAppBarForVehicles() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.show()
        }
    }

    private fun setAppBarForRocketDetails() {
        hideAppBar()
        binding.fab.hide()
    }

    private fun setAppBarForDragonDetails() {
        hideAppBar()
        binding.fab.hide()
    }

    private fun setAppBarForShipDetails() {
        hideAppBar()
        binding.fab.hide()
    }

    private fun setAppBarForCoreDetails() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
        }
    }

    private fun setAppBarForCapsuleDetails() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
        }
    }

    private fun setAppBarForStatistics(hasFilter: Boolean) {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            if (hasFilter) fab.show() else fab.hide()
        }
    }

    private fun setAppBarForHistory() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.show()
        }
    }

    private fun setAppBarForCompany() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
        }
    }

    private fun setAppBarForAbout() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
        }
    }

    private fun hideAppBar() {
        binding.run {
            bottomAppBar.performHide()
            bottomAppBar.animate().setListener(object : AnimatorListenerAdapter() {
                var isCanceled = false
                override fun onAnimationEnd(animation: Animator?) {
                    if (isCanceled) return

                    bottomAppBar.visibility = View.GONE
                    fab.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isCanceled = true
                }
            })
        }
    }
}
