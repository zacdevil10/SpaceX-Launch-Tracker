package uk.co.zac_h.spacex.launches

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_launches.*

import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.launches.adapters.LaunchesPagerAdapter

class LaunchesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launches, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launches_view_pager.adapter = LaunchesPagerAdapter(childFragmentManager)
        launches_tab_layout.setupWithViewPager(launches_view_pager)
    }
}
