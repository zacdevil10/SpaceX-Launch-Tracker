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

    private var _binding: FragmentCoreDetailsBinding? = null
    private val binding get() = _binding!!

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
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()

        title = core?.serial ?: title
        binding.toolbarLayout.toolbar.setup()

        updateCoreDetails(core)

        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("core", core)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateCoreDetails(core: Core?) {
        core?.apply {
            this@CoreDetailsFragment.core = core

            with(binding) {
                coreDetailsScrollview.transitionName = id

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

                toolbarLayout.progress.hide()
            }
        }
    }
}
