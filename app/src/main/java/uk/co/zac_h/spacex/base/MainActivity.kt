package uk.co.zac_h.spacex.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ActivityMainBinding
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class MainActivity : AppCompatActivity(),
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private lateinit var binding: ActivityMainBinding

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).preferencesRepo.themeModeLive.observe(this, Observer { mode ->
            mode?.let { delegate.localNightMode = it }
        })

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let {
            currentPosition = it.getInt("crew_pager_position")
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

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        (application as App).networkStateChangeListener.updateState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("crew_pager_position", currentPosition)
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
        var currentPosition: Int = 0
    }
}
