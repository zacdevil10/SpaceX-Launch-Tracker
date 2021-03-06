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
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentShipDetailsBinding
import uk.co.zac_h.spacex.launches.adapters.MissionsAdapter
import uk.co.zac_h.spacex.model.spacex.Ship
import uk.co.zac_h.spacex.utils.setImageAndTint

class ShipDetailsFragment : BaseFragment() {

    override var title: String = ""

    private var binding: FragmentShipDetailsBinding? = null

    private var ship: Ship? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()

        ship = arguments?.getParcelable("ship") as Ship?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentShipDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding?.apply {
            toolbar.setupWithNavController(navController, appBarConfig)

            ship?.let {
                shipDetailsCoordinator.transitionName = it.id
                title = it.name ?: ""
                toolbar.title = it.name

                Glide.with(view)
                    .load(it.image)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(header)

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
                    shipDetailsMassText.text = context?.getString(
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}