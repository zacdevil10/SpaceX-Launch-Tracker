package uk.co.zac_h.spacex.base

import android.os.Bundle
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.ApiResult
import uk.co.zac_h.spacex.utils.ApiResult.Status
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

abstract class BaseFragment : Fragment(),
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    open val title: String by lazy { getString(R.string.app_name) }

    protected var apiState: Status = Status.PENDING

    private lateinit var navController: NavController

    private lateinit var appBarConfig: AppBarConfiguration

    private val applicationContext by lazy { requireContext().applicationContext as App }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        appBarConfig = AppBarConfiguration.Builder(applicationContext.startDestinations)
            .setOpenableLayout(drawerLayout).build()
    }

    override fun onStart() {
        super.onStart()
        applicationContext.networkStateChangeListener.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        applicationContext.networkStateChangeListener.removeListener(this)
    }

    protected fun Toolbar.setup() {
        setupWithNavController(navController, appBarConfig)
        title = this@BaseFragment.title
    }

    protected fun Toolbar.setupWithTabLayout(
        tabLayout: TabLayout,
        tabLayoutMode: Int = TabLayout.MODE_FIXED
    ) {
        setup()
        tabLayout.tabMode = tabLayoutMode
    }

    protected fun setup(toolbar: Toolbar, toolbarLayout: CollapsingToolbarLayout) {
        NavigationUI.setupWithNavController(toolbarLayout, toolbar, navController, appBarConfig)
        toolbar.title = title
    }

    protected fun Toolbar.setSupportActionBar() {
        (activity as MainActivity).setSupportActionBar(this)
    }

    open fun Toolbar.createOptionsMenu(@MenuRes menu: Int) {
        inflateMenu(menu)
        setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
    }
}