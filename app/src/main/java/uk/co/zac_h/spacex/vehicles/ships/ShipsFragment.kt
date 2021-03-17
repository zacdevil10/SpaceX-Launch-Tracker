package uk.co.zac_h.spacex.vehicles.ships

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentCoreBinding
import uk.co.zac_h.spacex.databinding.FragmentShipsBinding
import uk.co.zac_h.spacex.model.spacex.Ship
import uk.co.zac_h.spacex.utils.ApiState
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom
import uk.co.zac_h.spacex.vehicles.adapters.ShipsAdapter

class ShipsFragment : BaseFragment(), NetworkInterface.View<List<Ship>> {

    override var title: String = "Ships"

    private var _binding: FragmentShipsBinding? = null
    private val binding get() = _binding!!

    private var presenter: NetworkInterface.Presenter<Nothing>? = null
    private lateinit var shipsAdapter: ShipsAdapter

    private lateinit var shipsArray: ArrayList<Ship>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shipsArray = savedInstanceState?.getParcelableArrayList("ships") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentShipsBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        presenter = ShipsPresenterImpl(this, ShipsInteractorImpl())

        shipsAdapter = ShipsAdapter(shipsArray)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this@ShipsFragment.context)
            setHasFixedSize(true)
            adapter = shipsAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            apiState = ApiState.PENDING
            presenter?.get()
        }

        if (shipsArray.isEmpty()) presenter?.get()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("ships", shipsArray)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        _binding = null
    }

    override fun update(response: List<Ship>) {
        apiState = ApiState.SUCCESS

        shipsArray.clear()
        shipsArray.addAll(response)

        binding.recycler.layoutAnimation = animateLayoutFromBottom(context)
        shipsAdapter.notifyDataSetChanged()
        binding.recycler.scheduleLayoutAnimation()
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        apiState = ApiState.FAILED
    }

    override fun networkAvailable() {
        when (apiState) {
            ApiState.PENDING, ApiState.FAILED -> presenter?.get()
            ApiState.SUCCESS -> Log.i(title, "Network available and data loaded")
        }
    }
}