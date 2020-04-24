package uk.co.zac_h.spacex.vehicles.capsules

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentCapsulesBinding
import uk.co.zac_h.spacex.model.spacex.CapsulesModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.vehicles.adapters.CapsulesAdapter

class CapsulesFragment : Fragment(), CapsulesContract.CapsulesView, SearchView.OnQueryTextListener,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentCapsulesBinding? = null
    private val binding get() = _binding!!

    private var presenter: CapsulesContract.CapsulesPresenter? = null

    private lateinit var capsulesAdapter: CapsulesAdapter
    private lateinit var capsulesArray: ArrayList<CapsulesModel>

    private var sortNew = false
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        capsulesArray =
            savedInstanceState?.getParcelableArrayList("capsules") ?: ArrayList()
        sortNew = savedInstanceState?.getBoolean("sort") ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCapsulesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = CapsulesPresenterImpl(this, CapsulesInteractorImpl())

        capsulesAdapter = CapsulesAdapter(capsulesArray)

        binding.capsulesRecycler.apply {
            layoutManager = LinearLayoutManager(this@CapsulesFragment.context)
            setHasFixedSize(true)
            adapter = capsulesAdapter
        }

        binding.capsulesSwipeRefresh.setOnRefreshListener {
            presenter?.getCapsules()
        }

        if (capsulesArray.isEmpty()) presenter?.getCapsules()
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
        outState.putParcelableArrayList("capsules", capsulesArray)
        outState.putBoolean("sort", sortNew)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequests()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_vehicles_cores, menu)

        searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.sort_new -> {
                if (!sortNew) {
                    sortNew = true
                    capsulesArray.reverse()
                    capsulesAdapter.notifyDataSetChanged()
                }
                true
            }
            R.id.sort_old -> {
                if (sortNew) {
                    sortNew = false
                    capsulesArray.reverse()
                    capsulesAdapter.notifyDataSetChanged()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onQueryTextSubmit(query: String?): Boolean {
        capsulesAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        capsulesAdapter.filter.filter(newText)
        return false
    }

    override fun updateCapsules(capsules: List<CapsulesModel>) {
        capsulesArray.clear()
        capsulesArray.addAll(if (sortNew) capsules.reversed() else capsules)

        binding.capsulesRecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom)
        capsulesAdapter.notifyDataSetChanged()
        binding.capsulesRecycler.scheduleLayoutAnimation()
    }

    override fun showProgress() {
        binding.capsulesProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.capsulesProgressBar.visibility = View.GONE
    }

    override fun toggleSwipeRefresh(refreshing: Boolean) {
        binding.capsulesSwipeRefresh.isRefreshing = refreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (capsulesArray.isEmpty() || binding.capsulesProgressBar.visibility == View.VISIBLE)
                presenter?.getCapsules()
        }
    }
}
