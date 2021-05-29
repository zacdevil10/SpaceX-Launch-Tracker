package uk.co.zac_h.spacex.statistics

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentStatisticsBinding
import uk.co.zac_h.spacex.statistics.adapters.StatisticsAdapter

class StatisticsFragment : BaseFragment(), StatisticsContract.View {

    override var title: String = "Statistics"

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentStatisticsBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.toolbarLayout.progress.hide()
        binding.toolbarLayout.toolbar.setup()

        binding.statisticsRecycler.apply {
            layoutManager = LinearLayoutManager(this@StatisticsFragment.context)
            adapter = StatisticsAdapter(this@StatisticsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun openWebLink(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }
}
