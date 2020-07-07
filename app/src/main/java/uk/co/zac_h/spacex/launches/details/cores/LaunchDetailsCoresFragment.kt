package uk.co.zac_h.spacex.launches.details.cores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsCoresBinding
import uk.co.zac_h.spacex.launches.adapters.FirstStageAdapter
import uk.co.zac_h.spacex.model.spacex.LaunchCoreExtendedModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchDetailsCoresFragment : Fragment(), LaunchDetailsCoresContract.View,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

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

        hideProgress()

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

    override fun onResume() {
        super.onResume()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("cores", cores)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
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
        binding.progressIndicator.show()
    }

    override fun hideProgress() {
        binding.progressIndicator.hide()
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            id?.let {
                if (cores.isEmpty()) presenter?.getLaunch(it)
            }
        }
    }
}