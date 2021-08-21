package uk.co.zac_h.spacex.launches.details.cores

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentVerticalRecyclerviewBinding
import uk.co.zac_h.spacex.launches.adapters.FirstStageAdapter
import uk.co.zac_h.spacex.model.spacex.LaunchCore
import uk.co.zac_h.spacex.utils.ApiResult
import uk.co.zac_h.spacex.utils.orUnknown

class LaunchDetailsCoresFragment : BaseFragment(), NetworkInterface.View<List<LaunchCore>> {

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private var presenter: NetworkInterface.Presenter<List<LaunchCore>>? = null

    private lateinit var coresAdapter: FirstStageAdapter
    private var cores: ArrayList<LaunchCore> = ArrayList()

    private lateinit var id: String

    companion object {
        const val CORES_KEY = "cores"
        const val ID_KEY = "id"

        fun newInstance(id: String) = LaunchDetailsCoresFragment().apply {
            this.id = id
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            id = it.getString(ID_KEY).orUnknown()
            cores = it.getParcelableArrayList(CORES_KEY) ?: ArrayList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchDetailsCoresPresenter(this, LaunchDetailsCoresInteractor())

        coresAdapter = FirstStageAdapter()

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coresAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            apiState = ApiResult.Status.PENDING
            presenter?.get(id)
        }

        presenter?.getOrUpdate(cores, id)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(CORES_KEY, cores)
        outState.putString(ID_KEY, id)
    }

    override fun onDestroyView() {
        presenter?.cancelRequest()
        super.onDestroyView()
    }

    override fun update(response: List<LaunchCore>) {
        apiState = ApiResult.Status.SUCCESS

        cores = response as ArrayList<LaunchCore>

        coresAdapter.update(response)
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    override fun showProgress() {
        binding.progress.show()
    }

    override fun hideProgress() {
        binding.progress.hide()
    }

    override fun showError(error: String) {
        apiState = ApiResult.Status.FAILURE
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        when (apiState) {
            ApiResult.Status.PENDING, ApiResult.Status.FAILURE -> presenter?.get(id)
            ApiResult.Status.SUCCESS -> Log.i(title, "Network available and data loaded")
        }
    }
}