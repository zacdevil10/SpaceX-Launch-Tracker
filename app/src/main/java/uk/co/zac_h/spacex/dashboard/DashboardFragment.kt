package uk.co.zac_h.spacex.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.dashboard.adapters.DashboardLaunchesAdapter
import uk.co.zac_h.spacex.utils.data.LaunchesModel

class DashboardFragment : Fragment(), DashboardView {

    private var presenter: DashboardPresenter? = null

    private lateinit var dashboardLaunchesAdapter: DashboardLaunchesAdapter
    private var launchArray = ArrayList<LaunchesModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashboardLaunchesAdapter = DashboardLaunchesAdapter(launchArray)

        presenter = DashboardPresenterImpl(this, DashboardInteractorImpl())

        dashboard_launches_recycler.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            setHasFixedSize(true)
            adapter = dashboardLaunchesAdapter
        }

        presenter?.apply {
            getSingleLaunch("next")
            getSingleLaunch("latest")
        }
    }

    override fun updateLaunchesList(launchesModel: LaunchesModel?) {
        if (launchesModel != null) {
            launchArray.add(launchesModel)
            dashboardLaunchesAdapter.notifyDataSetChanged()
        }
    }
}
