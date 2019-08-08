package uk.co.zac_h.spacex.launches

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_launches_list.*

import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.utils.data.LaunchesModel

class LaunchesListFragment : Fragment(), LaunchesView {

    private var presenter: LaunchesPresenter? = null
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

        if (param != null) presenter?.getLaunchList(param)
    }

    override fun updateLaunchesList(launches: List<LaunchesModel>?) {
        if (launches != null) {
            launchesList.addAll(if (arguments?.getString("launchParam") == "past") launches.asReversed() else launches)
        }

        launchesAdapter.notifyDataSetChanged()
    }

    override fun toggleProgress(visibility: Int) {

    }

    override fun toggleSwipeProgress(isRefreshing: Boolean) {

    }

    override fun showError(error: String) {

    }
}
