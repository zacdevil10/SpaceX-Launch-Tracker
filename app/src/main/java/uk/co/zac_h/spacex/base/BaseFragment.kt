package uk.co.zac_h.spacex.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.ApiState
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

abstract class BaseFragment : Fragment(),
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    abstract val title: String

    var apiState: ApiState = ApiState.PENDING

    lateinit var navController: NavController

    lateinit var appBarConfig: AppBarConfiguration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()
    }

    override fun onStart() {
        super.onStart()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    fun Toolbar.setup() {
        this.apply {
            setupWithNavController(navController, appBarConfig)
            title = this@BaseFragment.title
        }
    }

    fun Toolbar.setupWithTabLayout(tabLayout: TabLayout, tabLayoutMode: Int) {
        this.setup()
        tabLayout.tabMode = tabLayoutMode

    }

    fun setup(toolbar: Toolbar, toolbarLayout: CollapsingToolbarLayout) {
        NavigationUI.setupWithNavController(toolbarLayout, toolbar, navController, appBarConfig)
        toolbar.title = title
    }

    fun Toolbar.setSupportActionBar() {
        (activity as MainActivity).setSupportActionBar(this)
    }

}