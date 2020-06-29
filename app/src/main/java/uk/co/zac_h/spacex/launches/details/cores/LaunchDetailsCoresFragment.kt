package uk.co.zac_h.spacex.launches.details.cores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsCoresBinding
import uk.co.zac_h.spacex.launches.adapters.FirstStageAdapter
import uk.co.zac_h.spacex.model.spacex.LaunchCoreExtendedModel

class LaunchDetailsCoresFragment : Fragment(), LaunchDetailsCoresContract.View {

    private var _binding: FragmentLaunchDetailsCoresBinding? = null
    private val binding get() = _binding!!

    private var presenter: LaunchDetailsCoresContract.Presenter? = null

    private lateinit var coresAdapter: FirstStageAdapter
    private lateinit var cores: ArrayList<LaunchCoreExtendedModel>

    private var id: String? = null

    companion object {
        @JvmStatic
        fun newInstance(id: String) =
            LaunchDetailsCoresFragment().apply {
                arguments = Bundle().apply {
                    putString("id", id)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cores =
            savedInstanceState?.getParcelableArrayList<LaunchCoreExtendedModel>("cores")
                ?: ArrayList()
        id = arguments?.getString("id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaunchDetailsCoresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchDetailsCoresPresenter(this, LaunchDetailsCoresInteractor())

        coresAdapter = FirstStageAdapter(cores)

        binding.launchDetailsCoresRecycler.apply {
            layoutManager = LinearLayoutManager(this@LaunchDetailsCoresFragment.context)
            adapter = coresAdapter
        }

        if (cores.isEmpty()) id?.let {
            presenter?.getLaunch(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("cores", cores)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun updateCoresRecyclerView(coresList: List<LaunchCoreExtendedModel>?) {
        coresList?.let {
            cores.clear()
            cores.addAll(it)

            coresAdapter.notifyDataSetChanged()
        }
    }

    override fun showProgress() {
        binding.launchDetailsCoresProgress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.launchDetailsCoresProgress.visibility = View.GONE
    }

    override fun showError(error: String) {

    }
}