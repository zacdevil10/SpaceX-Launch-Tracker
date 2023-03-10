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
import com.google.android.material.transition.MaterialElevationScale
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.databinding.FragmentCoreDetailsBinding
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.vehicles.cores.Core
import uk.co.zac_h.spacex.vehicles.cores.CoreViewModel

class CoreDetailsFragment : BaseFragment(), ViewPagerFragment {

    override val title: String by lazy { navArgs.label ?: title }

    private val navArgs: CoreDetailsFragmentArgs by navArgs()

    private val viewModel: CoreViewModel by viewModels()

    private lateinit var binding: FragmentCoreDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()

        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
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
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.coreDetailsScrollview.transitionName = navArgs.id

        binding.coreDetailsMissionRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.cores.observe(viewLifecycleOwner) { result ->
            when (result) {
                is  ApiResult.Pending -> binding.progress.show()
                is ApiResult.Success -> result.data?.first { it.id == navArgs.id }?.let {
                    update(it)
                    binding.progress.hide()
                }
                is  ApiResult.Failure -> {}
            }
        }

        viewModel.getCores()
    }

    private fun update(core: Core?) {
        with(binding) {
            core?.let { core ->
                coreDetailsSerialText.text = core.serial
                coreDetailsBlockText.text = core.block ?: "TBD"
                coreDetailsDetailsText.text = core.lastUpdate
                coreDetailsStatusText.text = core.status?.status
                coreDetailsReuseText.text = core.reuseCount.toString()
                coreDetailsRtlsAttemptsText.text = core.attemptsRtls.toString()
                coreDetailsRtlsLandingsText.text = core.landingsRtls.toString()
                coreDetailsAsdsAttemptsText.text = core.attemptsAsds.toString()
                coreDetailsAsdsLandingsText.text = core.landingsAsds.toString()
            }
        }
    }
}
