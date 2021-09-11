package uk.co.zac_h.spacex.vehicles.cores.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentCoreDetailsBinding
import uk.co.zac_h.spacex.dto.spacex.Core
import uk.co.zac_h.spacex.launches.adapters.MissionsAdapter
import uk.co.zac_h.spacex.vehicles.cores.CoreViewModel

class CoreDetailsFragment : BaseFragment() {

    override val title: String by lazy { navArgs.label ?: title }

    private val navArgs: CoreDetailsFragmentArgs by navArgs()

    private val viewModel: CoreViewModel by viewModels()

    private lateinit var binding: FragmentCoreDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()

        viewModel.selectedId = navArgs.id
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

        viewModel.getCores()

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.toolbarLayout.toolbar.setup()

        viewModel.cores.observe(viewLifecycleOwner) { result ->
            result.data?.first { it.id == viewModel.selectedId }?.let { update(it) }
        }
    }

    private fun update(core: Core?) {
        core?.apply {
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
                        layoutManager = LinearLayoutManager(requireContext())
                        setHasFixedSize(true)
                        adapter = MissionsAdapter(context, it)
                    }
                }

                toolbarLayout.progress.hide()
            }
        }
    }
}
