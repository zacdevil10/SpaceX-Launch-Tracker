package uk.co.zac_h.spacex.vehicles.cores

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentCompanyBinding
import uk.co.zac_h.spacex.databinding.FragmentCoreBinding
import uk.co.zac_h.spacex.model.spacex.Core
import uk.co.zac_h.spacex.utils.ApiState
import uk.co.zac_h.spacex.utils.OrderSharedPreferencesHelper
import uk.co.zac_h.spacex.utils.OrderSharedPreferencesHelperImpl
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom
import uk.co.zac_h.spacex.vehicles.adapters.CoreAdapter

class CoreFragment : BaseFragment(), NetworkInterface.View<List<Core>>,
    SearchView.OnQueryTextListener {

    override var title: String = "Cores"

    private var _binding: FragmentCoreBinding? = null
    private val binding get() = _binding!!

    private var presenter: NetworkInterface.Presenter<Nothing>? = null
    private lateinit var coreAdapter: CoreAdapter

    private lateinit var coresArray: ArrayList<Core>

    private lateinit var orderSharedPreferences: OrderSharedPreferencesHelper
    private var sortNew = false
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        coresArray = savedInstanceState?.getParcelableArrayList("cores") ?: ArrayList()
        sortNew = savedInstanceState?.getBoolean("sort") ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCoreBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        orderSharedPreferences = OrderSharedPreferencesHelperImpl.build(context)
        presenter = CorePresenterImpl(this, CoreInteractorImpl())

        sortNew = orderSharedPreferences.isSortedNew("cores")

        coreAdapter = CoreAdapter(context, coresArray)

        binding.coreRecycler.apply {
            layoutManager = LinearLayoutManager(this@CoreFragment.context)
            setHasFixedSize(true)
            adapter = coreAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            apiState = ApiState.PENDING
            presenter?.get()
        }

        if (coresArray.isEmpty()) presenter?.get()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("cores", coresArray)
        outState.putBoolean("sort", sortNew)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
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
                    orderSharedPreferences.setSortOrder("cores", sortNew)
                    coresArray.reverse()
                    coreAdapter.notifyDataSetChanged()
                }
                true
            }
            R.id.sort_old -> {
                if (sortNew) {
                    sortNew = false
                    orderSharedPreferences.setSortOrder("cores", sortNew)
                    coresArray.reverse()
                    coreAdapter.notifyDataSetChanged()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onQueryTextSubmit(query: String?): Boolean {
        coreAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        coreAdapter.filter.filter(newText)
        return false
    }

    override fun update(response: List<Core>) {
        apiState = ApiState.SUCCESS

        coresArray.clear()
        coresArray.addAll(if (sortNew) response.reversed() else response)

        binding.coreRecycler.layoutAnimation = animateLayoutFromBottom(context)
        coreAdapter.notifyDataSetChanged()
        binding.coreRecycler.scheduleLayoutAnimation()
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
