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
import uk.co.zac_h.spacex.launches.adapters.MissionsAdapter
import uk.co.zac_h.spacex.vehicles.cores.Core
import uk.co.zac_h.spacex.vehicles.cores.CoreViewModel

class CoreDetailsFragment : BaseFragment() {

    override val title: String by lazy { navArgs.label ?: title }

    private val navArgs: CoreDetailsFragmentArgs by navArgs()

    private val viewModel: CoreViewModel by viewModels()

    private lateinit var binding: FragmentCoreDetailsBinding

    private lateinit var missionsAdapter: MissionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()
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

        binding.coreDetailsScrollview.transitionName = navArgs.id

        missionsAdapter = MissionsAdapter(requireContext())

        binding.coreDetailsMissionRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = missionsAdapter
        }

        viewModel.cores.observe(viewLifecycleOwner) { result ->
            result.data?.first { it.id == navArgs.id }?.let { update(it) }
        }
    }

    private fun update(core: Core?) {
        with(binding) {
            core?.let { core ->
                coreDetailsSerialText.text = core.serial
                coreDetailsBlockText.text = core.block ?: "TBD"
                coreDetailsDetailsText.text = core.lastUpdate
                core.status?.let {
                    coreDetailsStatusText.text = it.status
                }
                coreDetailsReuseText.text = core.reuseCount.toString()
                coreDetailsRtlsAttemptsText.text = core.attemptsRtls.toString()
                coreDetailsRtlsLandingsText.text = core.landingsRtls.toString()
                coreDetailsAsdsAttemptsText.text = core.attemptsAsds.toString()
                coreDetailsAsdsLandingsText.text = core.landingsAsds.toString()

                missionsAdapter.submitList(core.launches)
            }
        }
    }
}
