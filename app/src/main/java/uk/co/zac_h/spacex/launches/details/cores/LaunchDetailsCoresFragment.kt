package uk.co.zac_h.spacex.launches.details.cores

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsCoresBinding
import uk.co.zac_h.spacex.launches.adapters.FirstStageAdapter
import uk.co.zac_h.spacex.model.spacex.LaunchCore
import uk.co.zac_h.spacex.utils.ApiState
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom

class LaunchDetailsCoresFragment : BaseFragment(), NetworkInterface.View<List<LaunchCore>> {

    private lateinit var binding: FragmentLaunchDetailsCoresBinding

    private var presenter: NetworkInterface.Presenter<List<LaunchCore>>? = null

    private lateinit var coresAdapter: FirstStageAdapter
    private lateinit var cores: ArrayList<LaunchCore>

    private lateinit var id: String

    companion object {
        const val CORES_KEY = "cores"

        fun newInstance(id: String) = LaunchDetailsCoresFragment().apply {
            this.id = id
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        cores = savedInstanceState?.getParcelableArrayList(CORES_KEY) ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchDetailsCoresBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchDetailsCoresPresenter(this, LaunchDetailsCoresInteractor())

        coresAdapter = FirstStageAdapter()

        binding.launchDetailsCoresRecycler.apply {
            layoutManager = LinearLayoutManager(this@LaunchDetailsCoresFragment.context)
            adapter = coresAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            apiState = ApiState.PENDING
            cores.clear()
            presenter?.get(id)
        }

        presenter?.getOrUpdate(cores, id)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(CORES_KEY, cores)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun update(response: List<LaunchCore>) {
        apiState = ApiState.SUCCESS

        if (cores.isEmpty()) {
            cores.addAll(response)
            binding.launchDetailsCoresRecycler.layoutAnimation = animateLayoutFromBottom(context)
        }

        coresAdapter.update(response)
        binding.launchDetailsCoresRecycler.scheduleLayoutAnimation()
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        apiState = ApiState.FAILED
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        when (apiState) {
            ApiState.PENDING, ApiState.FAILED -> presenter?.get(id)
            ApiState.SUCCESS -> Log.i(title, "Network available and data loaded")
        }
    }
}