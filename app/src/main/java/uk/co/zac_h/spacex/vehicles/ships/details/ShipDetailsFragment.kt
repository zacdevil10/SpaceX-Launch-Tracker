package uk.co.zac_h.spacex.vehicles.ships.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.CollapsingToolbarBinding
import uk.co.zac_h.spacex.databinding.FragmentShipDetailsBinding
import uk.co.zac_h.spacex.dto.spacex.Ship
import uk.co.zac_h.spacex.launches.adapters.MissionsAdapter
import uk.co.zac_h.spacex.utils.setImageAndTint
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

        setup(toolbarBinding.toolbar, toolbarBinding.toolbarLayout)

        viewModel.ships.observe(viewLifecycleOwner) { result ->
            result.data?.first { it.id == viewModel.selectedId }?.let { update(it) }
        }
    }

    private fun update(ship: Ship?) {
        with(binding) {
            ship?.let {
                shipDetailsCoordinator.transitionName = it.id

                Glide.with(requireContext())
                    .load(it.image)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(toolbarBinding.header)

                when (it.active) {
                    true -> shipDetailsStatusImage.setImageAndTint(
                        R.drawable.ic_check_circle_black_24dp,
                        R.color.success
                    )
                    false -> shipDetailsStatusImage.setImageAndTint(
                        R.drawable.ic_remove_circle_black_24dp,
                        R.color.failed
                    )
                }

                shipDetailsTypeText.text = it.type
                shipDetailsRolesText.text = it.roles?.joinToString(", ")
                shipDetailsPortText.text = it.homePort

                it.yearBuilt?.let { yearBuilt ->
                    shipDetailsBuiltText.text = yearBuilt.toString()
                } ?: run {
                    shipDetailsBuiltLabel.visibility = View.GONE
                    shipDetailsBuiltText.visibility = View.GONE
                }

                it.mass?.let { mass ->
                    shipDetailsMassText.text = getString(
                        R.string.mass_formatted,
                        mass.kg,
                        mass.lb
                    )
                } ?: run {
                    shipDetailsMassLabel.visibility = View.GONE
                    shipDetailsMassText.visibility = View.GONE
                }

                it.launches?.let {
                    shipDetailsMissionRecycler.apply {
                        layoutManager = LinearLayoutManager(this@ShipDetailsFragment.context)
                        setHasFixedSize(true)
                        adapter = MissionsAdapter(context, it)
                    }
                } ?: run {
                    shipDetailsMissionLabel.visibility = View.GONE
                }

            }
        }
    }
}