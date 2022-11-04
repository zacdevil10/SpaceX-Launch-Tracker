package uk.co.zac_h.spacex.launch.details.cores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.core.ui.databinding.FragmentVerticalRecyclerviewBinding
import uk.co.zac_h.spacex.launch.LaunchesViewModel
import uk.co.zac_h.spacex.launch.R
import uk.co.zac_h.spacex.launch.adapters.FirstStageAdapter

class LaunchDetailsCoresFragment : BaseFragment(), ViewPagerFragment {

    override var title: String = "Cores"

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.launch_nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private lateinit var coresAdapter: FirstStageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progress.hide()

        coresAdapter = FirstStageAdapter()

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coresAdapter
        }

        coresAdapter.submitList(viewModel.cores)
    }
}
