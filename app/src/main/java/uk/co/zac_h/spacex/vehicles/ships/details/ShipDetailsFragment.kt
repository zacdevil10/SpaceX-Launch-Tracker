package uk.co.zac_h.spacex.vehicles.ships.details

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
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentShipDetailsBinding
import uk.co.zac_h.spacex.launches.adapters.MissionsAdapter
import uk.co.zac_h.spacex.model.spacex.ShipExtendedModel
import uk.co.zac_h.spacex.utils.metricFormat
import uk.co.zac_h.spacex.utils.setImageAndTint

class ShipDetailsFragment : Fragment() {

    private var _binding: FragmentShipDetailsBinding? = null
    private val binding get() = _binding!!

    private var ship: ShipExtendedModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()

        ship = arguments?.getParcelable("ship") as ShipExtendedModel?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShipDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding.toolbar.setupWithNavController(navController, appBarConfig)

        ship?.let {
            binding.shipDetailsCoordinator.transitionName = it.id
            binding.toolbar.title = it.name

            Glide.with(view)
                .load(it.image)
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(binding.header)

            when (it.active) {
                true -> binding.shipDetailsStatusImage.setImageAndTint(
                    R.drawable.ic_check_circle_black_24dp,
                    R.color.success
                )
                false -> binding.shipDetailsStatusImage.setImageAndTint(
                    R.drawable.ic_remove_circle_black_24dp,
                    R.color.failed
                )
            }

            binding.shipDetailsTypeText.text = it.type
            binding.shipDetailsRolesText.text = it.roles?.joinToString(", ")
            binding.shipDetailsPortText.text = it.homePort

            it.yearBuilt?.let { yearBuilt ->
                binding.shipDetailsBuiltText.text = yearBuilt.toString()
            } ?: run {
                binding.shipDetailsBuiltLabel.visibility = View.GONE
                binding.shipDetailsBuiltText.visibility = View.GONE
            }

            if (it.massKg != null && it.massLbs != null) {
                binding.shipDetailsMassText.text = context?.getString(
                    R.string.mass_formatted,
                    it.massKg?.metricFormat(),
                    it.massLbs?.metricFormat()
                )
            } else {
                binding.shipDetailsMassLabel.visibility = View.GONE
                binding.shipDetailsMassText.visibility = View.GONE
            }

            it.launches?.let {
                binding.shipDetailsMissionRecycler.apply {
                    layoutManager = LinearLayoutManager(this@ShipDetailsFragment.context)
                    setHasFixedSize(true)
                    adapter = MissionsAdapter(context, it)
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}