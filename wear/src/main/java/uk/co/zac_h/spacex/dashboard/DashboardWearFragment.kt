package uk.co.zac_h.spacex.dashboard

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_dashboard.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.data.LaunchesModel
import uk.co.zac_h.spacex.utils.format

class DashboardWearFragment : Fragment(), DashboardWearView {

    private lateinit var presenter: DashboardWearPresenter

    private var countdown: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!::presenter.isInitialized) presenter = DashboardWearPresenterImpl(this, DashboardWearInteractorImpl())

        presenter.apply {
            getLaunch("next")
            getLaunch("latest")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdown?.cancel()
        presenter.cancelRequests()
    }

    override fun updateNextLaunch(launch: LaunchesModel) {
        dashboard_name_text.text = launch.missionName
        dashboard_date_text.text = launch.tbd?.let {
            launch.launchDateUnix.format(it)
        } ?: launch.launchDateUnix.format()
        dashboard_flight_text.text =
            context?.getString(R.string.flight_number, launch.flightNumber)
    }

    override fun updateLatestLaunch(launch: LaunchesModel) {
        dashboard_latest_name_text.text = launch.missionName
        dashboard_latest_date_text.text = launch.tbd?.let {
            launch.launchDateUnix.format(it)
        } ?: launch.launchDateUnix.format()
        dashboard_latest_flight_text.text =
            context?.getString(R.string.flight_number, launch.flightNumber)
    }

    override fun setCountdown(launchDateUnix: Long) {
        val time = launchDateUnix.times(1000) - System.currentTimeMillis()

        countdown = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                presenter.updateCountdown(millisUntilFinished)
            }

            override fun onFinish() {
                dashboard_countdown_text.text = "Finished"
            }
        }.start()
    }

    override fun updateCountdown(countdown: String) {
        dashboard_countdown_text.text = countdown
    }

    override fun showNextProgress() {
        dashboard_next_progress_bar.visibility = View.VISIBLE
    }

    override fun showLatestProgress() {
        dashboard_latest_progress_bar.visibility = View.VISIBLE
    }

    override fun hideNextProgress() {
        dashboard_next_progress_bar.visibility = View.GONE
    }

    override fun hideLatestProgress() {
        dashboard_latest_progress_bar.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
