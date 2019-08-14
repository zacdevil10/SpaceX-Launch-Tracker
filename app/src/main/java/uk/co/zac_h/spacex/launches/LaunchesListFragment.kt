package uk.co.zac_h.spacex.launches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_launches_list.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.utils.data.LaunchesModel

class LaunchesListFragment : Fragment(), LaunchesView {

    private lateinit var presenter: LaunchesPresenter
    private lateinit var launchesAdapter: LaunchesAdapter
    private var launchesList = ArrayList<LaunchesModel>()

    companion object {
        fun newInstance(launchParam: String) = LaunchesListFragment().apply {
            arguments = Bundle().apply {
                putString("launchParam", launchParam)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launches_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchesPresenterImpl(this, LaunchesInteractorImpl())

        launchesAdapter = LaunchesAdapter(context, launchesList)

        launches_recycler.apply {
            layoutManager = LinearLayoutManager(this@LaunchesListFragment.context)
            setHasFixedSize(true)
            adapter = launchesAdapter
        }

        val param = arguments?.getString("launchParam")

        if (param != null) presenter.getLaunchList(param, if (param == "past") "desc" else "asc")

        launches_swipe_refresh.setOnRefreshListener {
            if (param != null) presenter.getLaunchList(param, if (param == "past") "desc" else "asc")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancelRequests()
    }

    override fun updateLaunchesList(launches: List<LaunchesModel>?) {
        if (launches != null) {
            launchesList.clear()
            launchesList.addAll(launches)
        }

        launchesAdapter.notifyDataSetChanged()
    }

    override fun toggleProgress(visibility: Int) {
        launches_progress_bar.visibility = visibility
    }

    override fun toggleSwipeProgress(isRefreshing: Boolean) {
        launches_swipe_refresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
