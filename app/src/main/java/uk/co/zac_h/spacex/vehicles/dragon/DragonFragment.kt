package uk.co.zac_h.spacex.vehicles.dragon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentDragonBinding
import uk.co.zac_h.spacex.model.spacex.Dragon
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom
import uk.co.zac_h.spacex.vehicles.adapters.DragonAdapter

class DragonFragment : BaseFragment(), NetworkInterface.View<List<Dragon>> {

    override var title: String = "Dragon"

    private var binding: FragmentDragonBinding? = null

    private var presenter: NetworkInterface.Presenter<Nothing>? = null
    private lateinit var dragonAdapter: DragonAdapter

    private lateinit var dragonArray: ArrayList<Dragon>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dragonArray = savedInstanceState?.getParcelableArrayList("dragon") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDragonBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        presenter = DragonPresenterImpl(this, DragonInteractorImpl())

        dragonAdapter = DragonAdapter(dragonArray)

        binding?.dragonRecycler?.apply {
            layoutManager = LinearLayoutManager(this@DragonFragment.context)
            setHasFixedSize(true)
            adapter = dragonAdapter
        }

        binding?.swipeRefresh?.setOnRefreshListener {
            presenter?.get()
        }

        if (dragonArray.isEmpty()) presenter?.get()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("dragon", dragonArray)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        binding = null
    }

    override fun update(response: List<Dragon>) {
        dragonArray.clear()
        dragonArray.addAll(response)

        binding?.dragonRecycler?.layoutAnimation = animateLayoutFromBottom(context)
        dragonAdapter.notifyDataSetChanged()
        binding?.dragonRecycler?.scheduleLayoutAnimation()
    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding?.swipeRefresh?.isRefreshing = isRefreshing
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            binding?.let {
                if (dragonArray.isEmpty()) presenter?.get()
            }
        }
    }

}
