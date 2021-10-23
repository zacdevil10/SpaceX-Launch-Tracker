package uk.co.zac_h.spacex.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.customview.widget.Openable
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.NavGraphDirections
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.about.history.filter.HistoryFilterFragment
import uk.co.zac_h.spacex.databinding.ActivityMainBinding
import uk.co.zac_h.spacex.utils.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener,
    Toolbar.OnMenuItemClickListener {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels()

    private val navBottomSheetBehavior: BottomSheetBehavior<NavigationView> by lazy {
        BottomSheetBehavior.from(binding.navView)
    }

    private val closeNavDrawerOnBackPressed by lazy {
        BottomSheetBackPressed(navBottomSheetBehavior)
    }

    private val openableNavView by lazy { BottomSheetOpenable(navBottomSheetBehavior) }

    private var bottomDrawerFragment: BottomDrawerFragment? = null

    private val openableBottomDrawer: Openable = object : Openable {
        override fun isOpen(): Boolean = false

        override fun open() {
            bottomDrawerFragment?.open()
        }

        override fun close() {
            bottomDrawerFragment?.close()
        }
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

        val appBarConfig = AppBarConfiguration
            .Builder(viewModel.startDestinations)
            .setOpenableLayout(openableNavView)
            .build()

        binding.bottomAppBar.setupWithNavController(navController, appBarConfig)
        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener(this)

        navBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        navBottomSheetBehavior.addBottomSheetCallback(BottomDrawerCallback().apply {
            addOnSlideAction(AlphaSlideAction(binding.scrim))
            addOnStateChangedAction(VisibilityStateAction(binding.scrim))
            addOnStateChangedAction(BackPressedStateAction(closeNavDrawerOnBackPressed))
            addOnStateChangedAction(ShowHideFabStateAction(binding.fab) { viewModel.isFabVisible })
            addOnStateChangedAction(ChangeSettingsMenuStateAction { showSettings ->
                binding.bottomAppBar.replaceMenu(
                    if (showSettings) R.menu.menu_settings else getBottomAppBarMenuForDestination()
                )
            })
            addOnStateChangedAction(HideBottomSheet(openableBottomDrawer))
        })

        binding.bottomAppBar.setOnMenuItemClickListener(this)

        binding.scrim.setOnClickListener {
            navBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        onBackPressedDispatcher.addCallback(this, closeNavDrawerOnBackPressed)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        //Setup toolbar for destination
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
        }
        //Close any open bottom sheets
        openableBottomDrawer.close()
        //Set toolbar menu for destination
        binding.bottomAppBar.replaceMenu(getBottomAppBarMenuForDestination(destination))

        setBottomDrawerForDestination(destination)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.edit -> {
                navigateToEditDashboard()
            }
            R.id.settings -> {
                navigateToSettings()
                openableNavView.close()
            }
        }
        return true
    }

    private fun setBottomDrawerForDestination(destination: NavDestination) {
        bottomDrawerFragment = when (destination.id) {
            R.id.history_page_fragment -> HistoryFilterFragment()
            else -> null
        }

        bottomDrawerFragment?.let { fragment ->
            supportFragmentManager.commit {
                replace(R.id.bottom_drawer, fragment)
            }
            binding.fab.setOnClickListener {
                fragment.toggle()
            }
            fragment.addOnStateChangedAction(
                ShowHideFabStateAction(binding.fab) {
                    viewModel.isFabVisible
                }
            )
        } ?: run {
            binding.fab.setOnClickListener(null)
        }
    }

    private fun setAppBarForDashboard() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
            viewModel.isFabVisible = false
        }
    }

    private fun setAppBarForLaunches() {
        binding.run {
            fab.setImageResource(R.drawable.ic_filter_list_black_24dp)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.show()
            viewModel.isFabVisible = true
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
            viewModel.isFabVisible = false
        }
    }

    private fun setAppBarForCrew() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
            viewModel.isFabVisible = false
        }
    }

    private fun setAppBarForCrewDetails() {
        hideAppBar()
        binding.fab.hide()
        viewModel.isFabVisible = false
    }

    private fun setAppBarForVehicles() {
        binding.run {
            fab.setImageResource(R.drawable.ic_sort_black_24dp)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.show()
            viewModel.isFabVisible = true
        }
    }

    private fun setAppBarForRocketDetails() {
        hideAppBar()
        binding.fab.hide()
        viewModel.isFabVisible = false
    }

    private fun setAppBarForDragonDetails() {
        hideAppBar()
        binding.fab.hide()
        viewModel.isFabVisible = false
    }

    private fun setAppBarForShipDetails() {
        hideAppBar()
        binding.fab.hide()
        viewModel.isFabVisible = false
    }

    private fun setAppBarForCoreDetails() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
            viewModel.isFabVisible = false
        }
    }

    private fun setAppBarForCapsuleDetails() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
            viewModel.isFabVisible = false
        }
    }

    private fun setAppBarForStatistics(hasFilter: Boolean) {
        binding.run {
            fab.setImageResource(R.drawable.ic_filter_list_black_24dp)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            if (hasFilter) {
                fab.show()
                viewModel.isFabVisible = true
            } else {
                fab.hide()
                viewModel.isFabVisible = false
            }
        }
    }

    private fun setAppBarForHistory() {
        binding.run {
            fab.setImageResource(R.drawable.ic_sort_black_24dp)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.show()
            viewModel.isFabVisible = true
        }
    }

    private fun setAppBarForCompany() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
            viewModel.isFabVisible = false
        }
    }

    private fun setAppBarForAbout() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            fab.hide()
            viewModel.isFabVisible = false
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

    @MenuRes
    private fun getBottomAppBarMenuForDestination(destination: NavDestination? = null): Int {
        val dest = destination ?: findNavController(R.id.nav_host).currentDestination
        return when (dest?.id) {
            R.id.dashboard_page_fragment -> R.menu.menu_dashboard
            else -> R.menu.menu_empty
        }
    }

    private fun navigateToSettings() {
        findNavController(R.id.nav_host).navigate(NavGraphDirections.actionToThemeDialog())
    }

    private fun navigateToEditDashboard() {
        findNavController(R.id.nav_host).navigate(NavGraphDirections.actionToDashboardEditDialog())
    }
}
