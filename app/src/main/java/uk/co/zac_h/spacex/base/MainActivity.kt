package uk.co.zac_h.spacex.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ActivityMainBinding
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class MainActivity : AppCompatActivity(),
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private lateinit var binding: ActivityMainBinding

    private val startDestinations = mutableSetOf(
        R.id.dashboard_page_fragment,
        R.id.news_page_fragment,
        R.id.launches_page_fragment,
        R.id.crew_page_fragment,
        R.id.vehicles_page_fragment,
        R.id.statistics_page_fragment,
        R.id.crew_detail_page_fragment
    )

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder(startDestinations).setDrawerLayout(drawerLayout).build()

        //findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController, appBarConfig)
        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)

        (application as App).preferencesRepo.themeModeLive.observe(this, Observer { mode ->
            mode?.let { delegate.localNightMode = it }
        })
    }

    override fun onStart() {
        super.onStart()
        (application as App).networkStateChangeListener.updateState()
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
}
