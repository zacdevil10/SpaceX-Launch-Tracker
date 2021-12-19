package uk.co.zac_h.spacex.vehicles.ships.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.CollapsingToolbarBinding
import uk.co.zac_h.spacex.databinding.FragmentShipDetailsBinding
import uk.co.zac_h.spacex.launches.adapters.MissionsAdapter
import uk.co.zac_h.spacex.utils.setImageAndTint
import uk.co.zac_h.spacex.vehicles.ships.Ship
import uk.co.zac_h.spacex.vehicles.ships.ShipsViewModel

class ShipDetailsFragment : BaseFragment() {

    override val title: String by lazy { navArgs.label ?: title }

    private val navArgs: ShipDetailsFragmentArgs by navArgs()

    private val viewModel: ShipsViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentShipDetailsBinding
    private lateinit var toolbarBinding: CollapsingToolbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()

        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentShipDetailsBinding.inflate(inflater, container, false).apply {
        toolbarBinding = CollapsingToolbarBinding.bind(this.root)
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getShips()

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        setup(toolbarBinding.toolbar, toolbarBinding.toolbarLayout)

        binding.shipDetailsCoordinator.transitionName = navArgs.id

        viewModel.ships.observe(viewLifecycleOwner) { result ->
            update(result.data?.first { it.id == navArgs.id })
        }
    }

    private fun update(ship: Ship?) {
        with(binding) {
            ship?.let { ship ->
                Glide.with(requireContext())
                    .load(ship.image)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(toolbarBinding.header)

                when (ship.active) {
                    true -> shipDetailsStatusImage.setImageAndTint(
                        R.drawable.ic_check_circle_black_24dp,
                        R.color.success
                    )
                    false -> shipDetailsStatusImage.setImageAndTint(
                        R.drawable.ic_remove_circle_black_24dp,
                        R.color.failed
                    )
                }

                shipDetailsTypeText.text = ship.type
                shipDetailsRolesText.text = ship.roles?.joinToString(", ")
                shipDetailsPortText.text = ship.homePort

                ship.yearBuilt?.let { yearBuilt ->
                    shipDetailsBuiltText.text = yearBuilt.toString()
                } ?: run {
                    shipDetailsBuiltLabel.visibility = View.GONE
                    shipDetailsBuiltText.visibility = View.GONE
                }

                ship.mass?.let { mass ->
                    shipDetailsMassText.text = getString(
                        R.string.mass_formatted,
                        mass.kg,
                        mass.lb
                    )
                } ?: run {
                    shipDetailsMassLabel.visibility = View.GONE
                    shipDetailsMassText.visibility = View.GONE
                }

                ship.launches?.let { launches ->
                    shipDetailsMissionRecycler.apply {
                        layoutManager = LinearLayoutManager(this@ShipDetailsFragment.context)
                        setHasFixedSize(true)
                        adapter = MissionsAdapter(context).also { it.submitList(launches) }
                    }
                } ?: run {
                    shipDetailsMissionLabel.visibility = View.GONE
                }

            }
        }
    }
}