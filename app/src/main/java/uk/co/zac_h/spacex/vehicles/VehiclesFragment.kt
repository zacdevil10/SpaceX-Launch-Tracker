package uk.co.zac_h.spacex.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.google.android.material.transition.Hold
import kotlinx.android.synthetic.main.fragment_vehicles.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.vehicles.adapters.VehiclesPagerAdapter

class VehiclesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = Hold()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_vehicles, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        vehicles_view_pager.apply {
            adapter = VehiclesPagerAdapter(childFragmentManager)
            offscreenPageLimit = 3
        }
        vehicles_tab_layout.setupWithViewPager(vehicles_view_pager)
    }
}
