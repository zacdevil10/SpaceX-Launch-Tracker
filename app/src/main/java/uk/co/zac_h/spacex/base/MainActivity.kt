package uk.co.zac_h.spacex.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).preferencesRepo.themeModeLive.observe(this) { mode ->
            mode?.let { delegate.localNightMode = it }
        }

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomAppBar.setupWithNavController(navController)
        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener(this)

        val navBottomSheetBehavior = BottomSheetBehavior.from(binding.navView)
        navBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.bottomAppBar.setNavigationOnClickListener {
            navBottomSheetBehavior.state = when (navBottomSheetBehavior.state) {
                BottomSheetBehavior.STATE_HIDDEN -> BottomSheetBehavior.STATE_HALF_EXPANDED
                else -> BottomSheetBehavior.STATE_HIDDEN
            }
        }
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
            //R.id.theme_alert_dialog ->
            //R.id.dashboard_edit_dialog ->
        }
    }

    private fun setAppBarForDashboard() {
        binding.run {
            bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
        }
    }

    private fun setAppBarForLaunches() {
        binding.run {
            bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
            fab.setImageResource(R.drawable.ic_filter_list_black_24dp)
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
            bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
        }
    }

    private fun setAppBarForCrew() {
        binding.run {
            bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
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
            bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
            fab.setImageResource(R.drawable.ic_sort_black_24dp)
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
            bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
        }
    }

    private fun setAppBarForCapsuleDetails() {
        binding.run {
            bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
        }
    }

    private fun setAppBarForStatistics(hasFilter: Boolean) {
        binding.run {
            bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
            fab.setImageResource(R.drawable.ic_filter_list_black_24dp)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            if (hasFilter) fab.show() else fab.hide()
        }
    }

    private fun setAppBarForHistory() {
        binding.run {
            bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            fab.setImageResource(R.drawable.ic_sort_black_24dp)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.show()
        }
    }

    private fun setAppBarForCompany() {
        binding.run {
            bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
        }
    }

    private fun setAppBarForAbout() {
        binding.run {
            bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
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
