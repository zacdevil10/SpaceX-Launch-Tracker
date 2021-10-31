package uk.co.zac_h.spacex.base

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.CollapsingToolbarLayout
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment : Fragment(),
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    open val title: String by lazy { getString(R.string.app_name) }

    @Inject
    lateinit var networkStateChangeListener: OnNetworkStateChangeListener

    override fun onStart() {
        super.onStart()
        networkStateChangeListener.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        networkStateChangeListener.removeListener(this)
    }

    protected fun setup(toolbar: Toolbar, toolbarLayout: CollapsingToolbarLayout) {
        NavigationUI.setupWithNavController(toolbarLayout, toolbar, findNavController())
        toolbar.title = title
    }
}