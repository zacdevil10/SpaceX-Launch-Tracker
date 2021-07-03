package uk.co.zac_h.spacex.launches.details.ships

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
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsShipsBinding
import uk.co.zac_h.spacex.launches.adapters.LaunchDetailsShipsAdapter
import uk.co.zac_h.spacex.model.spacex.Ship
import uk.co.zac_h.spacex.utils.ApiState
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom
import uk.co.zac_h.spacex.utils.clearAndAdd
import uk.co.zac_h.spacex.utils.orUnknown

class LaunchDetailsShipsFragment : BaseFragment(), NetworkInterface.View<List<Ship>> {

    private lateinit var binding: FragmentLaunchDetailsShipsBinding

    private var presenter: NetworkInterface.Presenter<Nothing>? = null

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
    ): View = FragmentLaunchDetailsShipsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchDetailsShipsPresenter(this, LaunchDetailsShipsInteractor())

        shipsAdapter = LaunchDetailsShipsAdapter(shipsArray)

        binding.launchDetailsShipsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = shipsAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            apiState = ApiState.PENDING
            presenter?.get(id)
        }

        if (shipsArray.isEmpty()) presenter?.get(id)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(SHIPS_KEY, shipsArray)
        outState.putString(ID_KEY, id)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun update(response: List<Ship>) {
        apiState = ApiState.SUCCESS

        shipsArray.clearAndAdd(response)

        binding.launchDetailsShipsRecycler.layoutAnimation =
            animateLayoutFromBottom(requireContext())
        shipsAdapter.notifyDataSetChanged()
        binding.launchDetailsShipsRecycler.scheduleLayoutAnimation()
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