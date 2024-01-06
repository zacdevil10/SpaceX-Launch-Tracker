package uk.co.zac_h.spacex.feature.launch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.network.TooManyRequestsException

@AndroidEntryPoint
class PreviousLaunchesListFragment : BaseFragment(), ViewPagerFragment {

    override val title: String by lazy { "Past" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            SpaceXTheme {

            }
        }
    }

    private fun showError(error: Throwable) {
        if (error !is TooManyRequestsException) {
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
        Log.e("PreviousLaunchesList", error.message.orUnknown())
    }

    override fun networkAvailable() {

    }
}
