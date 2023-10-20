package uk.co.zac_h.spacex.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
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

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).preferencesRepo.themeModeLive.observe(this) { mode ->
            mode?.let { delegate.localNightMode = it }
        }

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment

        networkStateChangeListener.apply {
            addListener(this@MainActivity)
            registerReceiver()
        }

        snackbar = Snackbar.make(
            binding.constraint,
            R.string.network_connection,
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            anchorView = binding.bottomNavigationView
            setAction(R.string.dismiss_label) { dismiss() }
        }

        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)
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
