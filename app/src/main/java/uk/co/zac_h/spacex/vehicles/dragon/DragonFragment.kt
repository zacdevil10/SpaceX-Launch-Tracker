package uk.co.zac_h.spacex.vehicles.dragon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentDragonBinding
import uk.co.zac_h.spacex.model.spacex.DragonModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.vehicles.adapters.DragonAdapter

class DragonFragment : Fragment(), DragonContract.DragonView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentDragonBinding? = null
    private val binding get() = _binding!!

    private var presenter: DragonContract.DragonPresenter? = null

    private lateinit var dragonAdapter: DragonAdapter
    private lateinit var dragonArray: ArrayList<DragonModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dragonArray = savedInstanceState?.getParcelableArrayList("dragon") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDragonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = DragonPresenterImpl(this, DragonInteractorImpl())

        dragonAdapter = DragonAdapter(dragonArray)

        binding.dragonRecycler.apply {
            layoutManager = LinearLayoutManager(this@DragonFragment.context)
            setHasFixedSize(true)
            adapter = dragonAdapter
        }

        binding.dragonSwipeRefresh.setOnRefreshListener {
            presenter?.getDragon()
        }

        if (dragonArray.isEmpty()) presenter?.getDragon()
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
        _binding = null
    }

    override fun updateDragon(dragon: List<DragonModel>) {
        dragonArray.clear()
        dragonArray.addAll(dragon)

        binding.dragonRecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom)
        dragonAdapter.notifyDataSetChanged()
        binding.dragonRecycler.scheduleLayoutAnimation()
    }

    override fun showProgress() {
        binding.dragonProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.dragonProgressBar.visibility = View.GONE
    }

    override fun toggleSwipeRefresh(refreshing: Boolean) {
        binding.dragonSwipeRefresh.isRefreshing = refreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (dragonArray.isEmpty() || binding.dragonProgressBar.visibility == View.VISIBLE) presenter?.getDragon()
        }
    }

}
