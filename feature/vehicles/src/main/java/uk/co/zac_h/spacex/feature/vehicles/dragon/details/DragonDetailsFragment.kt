package uk.co.zac_h.spacex.feature.vehicles.dragon.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialSharedAxis
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.image.setImageAndTint
import uk.co.zac_h.spacex.core.common.utils.metricFormat
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.feature.vehicles.R
import uk.co.zac_h.spacex.feature.vehicles.databinding.FragmentDragonDetailsBinding
import uk.co.zac_h.spacex.feature.vehicles.dragon.DragonViewModel
import uk.co.zac_h.spacex.feature.vehicles.dragon.SpacecraftItem

class DragonDetailsFragment : BaseFragment(), ViewPagerFragment {

    override val title: String by lazy { navArgs.label ?: title }

    private val navArgs: DragonDetailsFragmentArgs by navArgs()

    private val viewModel: DragonViewModel by navGraphViewModels(R.id.vehicles_nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentDragonDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDragonDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dragons.observe(viewLifecycleOwner) { result ->
            result.data?.first { it.id == viewModel.selectedId }?.let { update(it) }
        }

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun update(dragon: SpacecraftItem) {
        println(dragon)

        with(binding) {
            Glide.with(requireContext())
                .load(dragon.imageUrl)
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(dragonImage)

            toolbar.title = dragon.name

            dragonDetailsText.text = "${dragon.history}\n\n${dragon.details}"

            if (dragon.inUse == true) dragonDetailsStatusImage.setImageAndTint(
                R.drawable.ic_check_circle_black_24dp,
                R.color.success
            ) else dragonDetailsStatusImage.setImageAndTint(
                R.drawable.ic_remove_circle_black_24dp,
                R.color.failed
            )

            dragonDetailsCrewCapacity.value = dragon.crewCapacity?.toString()
            dragonDetailsFirstFlight.value = dragon.maidenFlight
            dragonDetailsHeight.value = dragon.height?.let { heightWithTrunk ->
                getString(R.string.measurements, heightWithTrunk.metricFormat())
            }
            dragonDetailsDiameter.value = dragon.diameter?.let { diameter ->
                getString(R.string.measurements, diameter.metricFormat())
            }
            dragonDetailsPayloadCapacity.value = dragon.payloadCapacity?.let { launchPayloadMass ->
                getString(R.string.mass, launchPayloadMass.metricFormat())
            }
        }
    }
}
