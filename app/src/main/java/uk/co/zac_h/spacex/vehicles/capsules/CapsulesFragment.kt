package uk.co.zac_h.spacex.vehicles.capsules

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentCapsulesBinding
import uk.co.zac_h.spacex.dto.spacex.Capsule
import uk.co.zac_h.spacex.utils.OrderSharedPreferences
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom
import uk.co.zac_h.spacex.vehicles.adapters.CapsulesAdapter

class CapsulesFragment : BaseFragment(), NetworkInterface.View<List<Capsule>>,
    SearchView.OnQueryTextListener {

    override var title: String = "Capsules"

    private lateinit var binding: FragmentCapsulesBinding

    private var presenter: NetworkInterface.Presenter<Nothing>? = null
    private lateinit var capsulesAdapter: CapsulesAdapter

    private var capsulesArray: ArrayList<Capsule> = ArrayList()

    private lateinit var orderSharedPreferences: OrderSharedPreferences
    private var sortNew = false
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        /*capsulesArray =
            savedInstanceState?.getParcelableArrayList("capsules") ?: ArrayList()*/
        sortNew = savedInstanceState?.getBoolean("sort") ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCapsulesBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderSharedPreferences = OrderSharedPreferences.build(requireContext())
        presenter = CapsulesPresenterImpl(this, CapsulesInteractorImpl())

        sortNew = orderSharedPreferences.isSortedNew("capsules")

        capsulesAdapter = CapsulesAdapter(capsulesArray)

        binding.capsulesRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = capsulesAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {

            presenter?.get()
        }

        if (capsulesArray.isEmpty()) presenter?.get()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            //putParcelableArrayList("capsules", capsulesArray)
            putBoolean("sort", sortNew)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
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
                    orderSharedPreferences.setSortOrder("capsules", sortNew)
                    capsulesArray.reverse()
                    capsulesAdapter.notifyDataSetChanged()
                }
                true
            }
            R.id.sort_old -> {
                if (sortNew) {
                    sortNew = false
                    orderSharedPreferences.setSortOrder("capsules", sortNew)
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

    override fun update(response: List<Capsule>) {


        capsulesArray.clear()
        capsulesArray.addAll(if (sortNew) response.reversed() else response)

        binding.capsulesRecycler.layoutAnimation = animateLayoutFromBottom(requireContext())
        capsulesAdapter.notifyDataSetChanged()
        binding.capsulesRecycler.scheduleLayoutAnimation()
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        /*when (apiState) {
            ApiResult.Status.PENDING, ApiResult.Status.FAILURE -> presenter?.get()
            ApiResult.Status.SUCCESS -> Log.i(title, "Network available and data loaded")
        }*/
    }
}
