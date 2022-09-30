package uk.co.zac_h.spacex.crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.navigation.navGraphViewModels
import com.google.android.material.transition.MaterialElevationScale
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.crew.adapters.CrewAdapter
import uk.co.zac_h.spacex.databinding.FragmentCrewBinding
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom

class CrewFragment : BaseFragment() {

    private val viewModel: CrewViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentCrewBinding

    private lateinit var crewAdapter: CrewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCrewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        viewModel.get()

        if (savedInstanceState == null) {
            binding.root.doOnPreDraw { startPostponedEnterTransition() }
        }

        crewAdapter = CrewAdapter()

        binding.crewRecycler.apply {
            setHasFixedSize(true)
            adapter = crewAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.get(CachePolicy.ALWAYS)
        }

        viewModel.crew.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Pending -> {}
                is ApiResult.Success -> {
                    binding.swipeRefresh.isRefreshing = false
                    update(result.data)
                }
                is ApiResult.Failure -> {
                    binding.swipeRefresh.isRefreshing = false
                    showError(result.exception.message)
                }
            }
        }
    }

    private fun update(response: List<Crew>?) {
        crewAdapter.submitList(response)
        if (viewModel.cacheLocation == Repository.RequestLocation.REMOTE) {
            binding.crewRecycler.layoutAnimation = animateLayoutFromBottom(requireContext())
            binding.crewRecycler.scheduleLayoutAnimation()
        }
    }

    private fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.get()
    }
}
