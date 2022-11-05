package uk.co.zac_h.spacex.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.NavGraphDirections
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.about.history.filter.HistoryFilterFragment
import uk.co.zac_h.spacex.core.common.bottomsheet.*
import uk.co.zac_h.spacex.core.common.fragment.BottomDrawerFragment
import uk.co.zac_h.spacex.core.common.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.databinding.ActivityMainBinding
import uk.co.zac_h.spacex.feature.launch.LaunchesFragmentDirections
import uk.co.zac_h.spacex.feature.vehicles.VehiclesFilterFragment
import uk.co.zac_h.spacex.statistics.graphs.launchhistory.filter.LaunchHistoryFilterFragment
import uk.co.zac_h.spacex.statistics.graphs.launchmass.filter.LaunchMassFilterFragment
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener,
    Toolbar.OnMenuItemClickListener, OnNetworkStateChangeListener.NetworkStateReceiverListener {

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

    private var snackbar: Snackbar? = null

    @Inject
    lateinit var networkStateChangeListener: OnNetworkStateChangeListener

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

        networkStateChangeListener.apply {
            addListener(this@MainActivity)
            registerReceiver()
        }

        snackbar = Snackbar.make(
            binding.coordinator,
            R.string.network_connection,
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            anchorView = binding.bottomAppBar
            setAction(R.string.dismiss_label) { dismiss() }
        }

        binding.bottomAppBar.setupWithNavController(navController, appBarConfig)
        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener(this)

        navBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        navBottomSheetBehavior.addBottomSheetCallback(BottomDrawerCallback().apply {
            addOnSlideAction(AlphaSlideAction(binding.scrim))
            addOnStateChangedAction(VisibilityStateAction(binding.scrim))
            addOnStateChangedAction(BackPressedStateAction(closeNavDrawerOnBackPressed))
            addOnStateChangedAction(ShowHideFabStateAction(binding.toolbarFab) { viewModel.isFabVisible })
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

    override fun onStart() {
        super.onStart()
        networkStateChangeListener.updateState()
    }

    override fun onDestroy() {
        super.onDestroy()
        snackbar = null
        networkStateChangeListener.apply {
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

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        //Close keyboard
        val ime = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        ime.hideSoftInputFromWindow(binding.root.windowToken, 0)
        //Close any open bottom sheets
        openableBottomDrawer.close()
        //Set toolbar menu for destination
        binding.bottomAppBar.replaceMenu(getBottomAppBarMenuForDestination(destination))

        setBottomDrawerForDestination(destination)

        //Setup toolbar for destination
        when (destination.id) {
            //Launches
            R.id.launches_page_fragment -> setAppBarForLaunches()
            R.id.launch_details_container_fragment -> setAppBarForLaunchDetails()
            R.id.launches_filter_fragment -> setAppBarForSearch()
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
            R.id.vehicles_fragment -> setAppBarForVehicles()
            R.id.rocket_details_fragment -> setAppBarForRocketDetails()
            R.id.dragon_details_fragment -> setAppBarForDragonDetails()
            R.id.core_details_fragment -> setAppBarForCoreDetails()
            R.id.capsule_details_fragment -> setAppBarForCapsuleDetails()
            //About
            R.id.company_fragment -> setAppBarForCompany()
            R.id.history_page_fragment -> setAppBarForHistory()
            R.id.about_fragment -> setAppBarForAbout()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.settings -> {
                navigateToSettings()
                openableNavView.close()
            }
        }
        return true
    }

    private fun setBottomDrawerForDestination(destination: NavDestination) {
        bottomDrawerFragment = when (destination.id) {
            R.id.launch_history_fragment -> LaunchHistoryFilterFragment()
            R.id.launch_mass_fragment -> LaunchMassFilterFragment()
            R.id.vehicles_fragment -> VehiclesFilterFragment()
            R.id.history_page_fragment -> HistoryFilterFragment()
            else -> null
        }

        bottomDrawerFragment?.let { fragment ->
            supportFragmentManager.commit {
                replace(R.id.bottom_drawer, fragment)
            }
            binding.toolbarFab.setOnClickListener {
                fragment.toggle()
            }
            fragment.addOnStateChangedAction(
                ShowHideFabStateAction(binding.toolbarFab) { viewModel.isFabVisible }
            )
        } ?: run {
            binding.toolbarFab.setOnClickListener(null)
        }
    }

    private fun setAppBarForLaunches() {
        binding.run {
            toolbarFab.setOnClickListener {
                findNavController(R.id.nav_host).navigate(LaunchesFragmentDirections.actionLaunchesToFilter())
            }
            toolbarFab.setImageResource(R.drawable.ic_search_black_24dp)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            toolbarFab.hide()
            viewModel.isFabVisible = false
        }
    }

    private fun setAppBarForLaunchDetails() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            toolbarFab.hide()
            viewModel.isFabVisible = false
        }
    }

    private fun setAppBarForNews() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            toolbarFab.hide()
            viewModel.isFabVisible = false
        }
    }

    private fun setAppBarForCrew() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            toolbarFab.hide()
            viewModel.isFabVisible = false
        }
    }

    private fun setAppBarForCrewDetails() {
        hideAppBar()
        binding.toolbarFab.hide()
        viewModel.isFabVisible = false
    }

    private fun setAppBarForVehicles() {
        binding.run {
            toolbarFab.setImageResource(R.drawable.ic_sort_black_24dp)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            toolbarFab.show()
            viewModel.isFabVisible = true
        }
    }

    private fun setAppBarForRocketDetails() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            toolbarFab.hide()
            viewModel.isFabVisible = false
        }
    }

    private fun setAppBarForDragonDetails() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            toolbarFab.hide()
            viewModel.isFabVisible = false
        }
    }

    private fun setAppBarForCoreDetails() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            toolbarFab.hide()
            viewModel.isFabVisible = false
        }
    }

    private fun setAppBarForCapsuleDetails() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            toolbarFab.hide()
            viewModel.isFabVisible = false
        }
    }

    private fun setAppBarForStatistics(hasFilter: Boolean) {
        binding.run {
            toolbarFab.setImageResource(R.drawable.ic_filter_list_black_24dp)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            if (hasFilter) {
                toolbarFab.show()
                viewModel.isFabVisible = true
            } else {
                toolbarFab.hide()
                viewModel.isFabVisible = false
            }
        }
    }

    private fun setAppBarForHistory() {
        binding.run {
            toolbarFab.setImageResource(R.drawable.ic_sort_black_24dp)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            toolbarFab.show()
            viewModel.isFabVisible = true
        }
    }

    private fun setAppBarForCompany() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            toolbarFab.hide()
            viewModel.isFabVisible = false
        }
    }

    private fun setAppBarForAbout() {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.performShow()
            toolbarFab.hide()
            viewModel.isFabVisible = false
        }
    }

    private fun setAppBarForSearch() {
        hideAppBar()
    }

    private fun hideAppBar() {
        binding.run {
            bottomAppBar.performHide()
            bottomAppBar.animate().setListener(object : AnimatorListenerAdapter() {
                var isCanceled = false
                override fun onAnimationEnd(animation: Animator) {
                    if (isCanceled) return

                    bottomAppBar.visibility = View.GONE
                    toolbarFab.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(animation: Animator) {
                    isCanceled = true
                }
            })
        }
    }

    @MenuRes
    private fun getBottomAppBarMenuForDestination(destination: NavDestination? = null): Int {
        val dest = destination ?: findNavController(R.id.nav_host).currentDestination
        return when (dest?.id) {
            else -> R.menu.menu_empty
        }
    }

    private fun navigateToSettings() {
        findNavController(R.id.nav_host).navigate(NavGraphDirections.actionToThemeDialog())
    }
}
