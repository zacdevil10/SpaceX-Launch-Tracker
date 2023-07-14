package uk.co.zac_h.spacex.base

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.core.common.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.databinding.ActivityMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var snackbar: Snackbar? = null

    @Inject
    lateinit var networkStateChangeListener: OnNetworkStateChangeListener

    private val startDestinations = mutableSetOf(
        R.id.launches_page_fragment,
        R.id.news_page_fragment,
        R.id.astronauts_fragment,
        R.id.vehicles_fragment
    )

    private val fullscreenDestinations = setOf(
        R.id.company_fragment,
        R.id.about_fragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).preferencesRepo.themeModeLive.observe(this) { mode ->
            mode?.let { delegate.localNightMode = it }
        }

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        networkStateChangeListener.apply {
            addListener(this@MainActivity)
            registerReceiver()
        }

        snackbar = Snackbar.make(
            binding.coordinator,
            R.string.network_connection,
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            anchorView = binding.bottomNavigationView
            setAction(R.string.dismiss_label) { dismiss() }
        }

        val appBarConfig = AppBarConfiguration.Builder(startDestinations)
            .setOpenableLayout(binding.drawerLayout)
            .build()

        binding.toolbar.setupWithNavController(navController, appBarConfig)
        binding.navView.setupWithNavController(navController)
        binding.bottomNavigationView.setupWithNavController(navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            toggleBottomNavigationView(destination.id !in fullscreenDestinations)
        }
    }

    private fun toggleBottomNavigationView(isVisible: Boolean) {
        if (binding.bottomNavigationView.isVisible != isVisible) {
            TransitionManager.beginDelayedTransition(
                binding.root,
                Slide(Gravity.BOTTOM).excludeTarget(R.id.nav_host, true)
            )

            binding.bottomNavigationView.isVisible = isVisible
        }
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
}
