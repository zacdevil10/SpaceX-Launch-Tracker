package uk.co.zac_h.spacex.statistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_statistics.*

import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.statistics.adapters.StatisticsPagerAdapter

class StatisticsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statistics_view_pager.adapter = StatisticsPagerAdapter(childFragmentManager)
    }
}
