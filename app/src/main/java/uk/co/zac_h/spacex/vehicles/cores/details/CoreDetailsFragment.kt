package uk.co.zac_h.spacex.vehicles.cores.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentCoreDetailsBinding
import uk.co.zac_h.spacex.launches.adapters.MissionsAdapter
import uk.co.zac_h.spacex.model.spacex.Core
import uk.co.zac_h.spacex.model.spacex.CoreStatus
import uk.co.zac_h.spacex.utils.*
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import java.util.*

class CoreDetailsFragment : Fragment(), OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var binding: FragmentCoreDetailsBinding? = null

    private var core: Core? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()

        core = if (savedInstanceState != null) {
            savedInstanceState.getParcelable("core")
        } else {
            arguments?.getParcelable("core")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoreDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        core?.let {
            updateCoreDetails(it)
        }

        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onResume() {
        super.onResume()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("core", core)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun updateCoreDetails(core: Core) {
        core.apply {
            this@CoreDetailsFragment.core = core

            binding?.apply {
                coreDetailsScrollview.transitionName = id

                toolbar.title = serial

                coreDetailsSerialText.text = serial
                coreDetailsBlockText.text = block ?: "TBD"
                coreDetailsDetailsText.text = lastUpdate
                status?.let {
                    coreDetailsStatusText.text = when (it) {
                        CoreStatus.ACTIVE -> SPACEX_CORE_STATUS_ACTIVE
                        CoreStatus.INACTIVE -> SPACEX_CORE_STATUS_INACTIVE
                        CoreStatus.UNKNOWN -> SPACEX_CORE_STATUS_UNKNOWN
                        CoreStatus.EXPENDED -> SPACEX_CORE_STATUS_EXPENDED
                        CoreStatus.LOST -> SPACEX_CORE_STATUS_LOST
                        CoreStatus.RETIRED -> SPACEX_CORE_STATUS_RETIRED
                    }.capitalize(Locale.getDefault())
                }
                coreDetailsReuseText.text = reuseCount.toString()
                coreDetailsRtlsAttemptsText.text = attemptsRtls.toString()
                coreDetailsRtlsLandingsText.text = landingsRtls.toString()
                coreDetailsAsdsAttemptsText.text = attemptsAsds.toString()
                coreDetailsAsdsLandingsText.text = landingsAsds.toString()

                missions?.let {
                    coreDetailsMissionRecycler.apply {
                        layoutManager = LinearLayoutManager(this@CoreDetailsFragment.context)
                        setHasFixedSize(true)
                        adapter = MissionsAdapter(context, it)
                    }
                }
            }
        }
    }
}
