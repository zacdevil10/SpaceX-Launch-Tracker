package uk.co.zac_h.spacex.core.fragment

import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.core.network.OnNetworkStateChangeListener
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment : Fragment(),
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

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
}
