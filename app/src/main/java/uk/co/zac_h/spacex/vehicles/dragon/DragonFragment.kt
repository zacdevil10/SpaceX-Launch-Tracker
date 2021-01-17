package uk.co.zac_h.spacex.vehicles.dragon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentDragonBinding
import uk.co.zac_h.spacex.model.spacex.Dragon
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.vehicles.adapters.DragonAdapter

class DragonFragment : Fragment(), NetworkInterface.View<List<Dragon>>,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    companion object {
        const val TITLE = "Dragon"
    }

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

        binding?.dragonSwipeRefresh?.setOnRefreshListener {
            presenter?.get()
        }

        if (dragonArray.isEmpty()) presenter?.get()
    }

    override fun onStart() {
        super.onStart()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
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

        binding?.dragonRecycler?.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom)
        dragonAdapter.notifyDataSetChanged()
        binding?.dragonRecycler?.scheduleLayoutAnimation()
    }

    override fun showProgress() {
        binding?.progressIndicator?.show()
    }

    override fun hideProgress() {
        binding?.progressIndicator?.hide()
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding?.dragonSwipeRefresh?.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            binding?.let {
                if (dragonArray.isEmpty() || it.progressIndicator.isShown) presenter?.get()
            }
        }
    }

}
