package uk.co.zac_h.spacex.launches.details.ships

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class LaunchDetailsShipsFragment : BaseFragment(), NetworkInterface.View<List<Ship>> {

    override var title: String = ""

    private lateinit var binding: FragmentLaunchDetailsShipsBinding

    private var presenter: NetworkInterface.Presenter<Nothing>? = null

    private lateinit var shipsAdapter: LaunchDetailsShipsAdapter
    private lateinit var shipsArray: ArrayList<Ship>

    private var id: String? = null

    companion object {
        @JvmStatic
        fun newInstance(args: Any) = LaunchDetailsShipsFragment().apply {
            arguments = bundleOf("id" to args)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shipsArray = savedInstanceState?.getParcelableArrayList("ships") ?: ArrayList()
        id = arguments?.getString("id")
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
            setHasFixedSize(true)
            adapter = shipsAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            apiState = ApiState.PENDING
            id?.let { presenter?.get(it) }
        }

        if (shipsArray.isEmpty()) id?.let { presenter?.get(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("ships", shipsArray)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun update(response: List<Ship>) {
        apiState = ApiState.SUCCESS

        shipsArray.clearAndAdd(response)

        binding.launchDetailsShipsRecycler.layoutAnimation = animateLayoutFromBottom(requireContext())
        shipsAdapter.notifyDataSetChanged()
        binding.launchDetailsShipsRecycler.scheduleLayoutAnimation()
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        apiState = ApiState.FAILED
    }

    override fun networkAvailable() {
        when(apiState) {
            ApiState.PENDING, ApiState.FAILED -> id?.let { presenter?.get(it) }
            ApiState.SUCCESS -> {}
        }
    }
}