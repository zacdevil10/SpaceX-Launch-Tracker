package uk.co.zac_h.spacex.launches.details.ships

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
import uk.co.zac_h.spacex.launches.adapters.LaunchDetailsShipsAdapter
import uk.co.zac_h.spacex.model.spacex.Ship
import uk.co.zac_h.spacex.utils.ApiResult
import uk.co.zac_h.spacex.utils.orUnknown

class LaunchDetailsShipsFragment : BaseFragment(), NetworkInterface.View<List<Ship>> {

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private var presenter: NetworkInterface.Presenter<List<Ship>>? = null

    private lateinit var shipsAdapter: LaunchDetailsShipsAdapter
    private var shipsArray: ArrayList<Ship> = ArrayList()

    private lateinit var id: String

    companion object {
        const val SHIPS_KEY = "ships"
        const val ID_KEY = "id"

        @JvmStatic
        fun newInstance(id: String) = LaunchDetailsShipsFragment().apply {
            this.id = id
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            shipsArray = it.getParcelableArrayList(SHIPS_KEY) ?: ArrayList()
            id = it.getString(ID_KEY).orUnknown()
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

        presenter = LaunchDetailsShipsPresenter(this, LaunchDetailsShipsInteractor())

        shipsAdapter = LaunchDetailsShipsAdapter()

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = shipsAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            apiState = ApiResult.Status.PENDING
            presenter?.get(id)
        }

        presenter?.getOrUpdate(shipsArray, id)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(SHIPS_KEY, shipsArray)
        outState.putString(ID_KEY, id)
    }

    override fun onDestroyView() {
        presenter?.cancelRequest()
        super.onDestroyView()
    }

    override fun update(response: List<Ship>) {
        apiState = ApiResult.Status.SUCCESS

        shipsArray = response as ArrayList<Ship>

        shipsAdapter.update(response)
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