package uk.co.zac_h.spacex.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uk.co.zac_h.spacex.databinding.FragmentStatisticsBinding
import uk.co.zac_h.spacex.statistics.adapters.StatisticsPagerAdapter

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.statisticsViewPager.apply {
            adapter = StatisticsPagerAdapter(childFragmentManager)
            offscreenPageLimit = 2
        }
        binding.statisticsTabLayout.setupWithViewPager(binding.statisticsViewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
