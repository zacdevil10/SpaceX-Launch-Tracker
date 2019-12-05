package uk.co.zac_h.spacex.launches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.wear.widget.WearableLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_launches_wear.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.launches.adapter.LaunchesWearAdapter
import uk.co.zac_h.spacex.model.spacex.LaunchesModel

class LaunchesWearFragment : Fragment(), LaunchesWearView {

    private lateinit var presenter: LaunchesWearPresenter

    companion object {
        fun newInstance(launchParam: String) = LaunchesWearFragment().apply {
            arguments = Bundle().apply {
                putString("launchParam", launchParam)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_launches_wear, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchesWearPresenterImpl(this, LaunchesWearInteractorImpl())

        arguments?.getString("launchParam")?.let { launchId ->
            presenter.getLaunches(launchId)

            launches_reload_button.setOnClickListener {
                presenter.getLaunches(launchId)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancelRequests()
    }

    override fun updateLaunches(launches: List<LaunchesModel>) {
        launches_recycler.apply {
            layoutManager = WearableLinearLayoutManager(this@LaunchesWearFragment.context)
            setHasFixedSize(true)
            isEdgeItemsCenteringEnabled = true
            adapter = LaunchesWearAdapter(this@LaunchesWearFragment.context, launches)
        }
    }

    override fun showProgress() {
        launches_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        launches_progress_bar.visibility = View.GONE
    }

    override fun showReload() {
        launches_reload_button.visibility = View.VISIBLE
    }

    override fun hideReload() {
        launches_reload_button.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

}
