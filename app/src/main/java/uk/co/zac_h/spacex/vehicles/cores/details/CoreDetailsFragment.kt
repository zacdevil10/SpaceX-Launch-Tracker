package uk.co.zac_h.spacex.vehicles.cores.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentCoreDetailsBinding
import uk.co.zac_h.spacex.launches.adapters.MissionsAdapter
import uk.co.zac_h.spacex.model.spacex.Core
import uk.co.zac_h.spacex.utils.*
import java.util.*

class CoreDetailsFragment : BaseFragment() {

    override var title: String = "Core Details"

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
    ): View = FragmentCoreDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        core?.let {
            updateCoreDetails(it)
        }

        view.doOnPreDraw { startPostponedEnterTransition() }
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
                    coreDetailsStatusText.text = it.status
                }
                coreDetailsReuseText.text = reuseCount.toString()
                coreDetailsRtlsAttemptsText.text = attemptsRtls.toString()
                coreDetailsRtlsLandingsText.text = landingsRtls.toString()
                coreDetailsAsdsAttemptsText.text = attemptsAsds.toString()
                coreDetailsAsdsLandingsText.text = landingsAsds.toString()

                launches?.let {
                    coreDetailsMissionRecycler.apply {
                        layoutManager = LinearLayoutManager(this@CoreDetailsFragment.context)
                        setHasFixedSize(true)
                        adapter = MissionsAdapter(context, it)
                    }
                }

                progress.hide()
            }
        }
    }
}
