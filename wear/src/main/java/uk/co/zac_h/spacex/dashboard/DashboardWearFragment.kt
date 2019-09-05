package uk.co.zac_h.spacex.dashboard

import android.app.Fragment
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.*
import retrofit2.HttpException

import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.data.LaunchesModel
import uk.co.zac_h.spacex.utils.data.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.format
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class DashboardWearFragment : Fragment() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private var countdown: CountDownTimer? = null

    private var launch: LaunchesModel? = null
    private var latest: LaunchesModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dashboard, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashboard_name_text.isSelected = true

        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                SpaceXInterface.create().getSingleLaunch("next")
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        launch = response.await().body()
                        setCountdown()
                        setData()
                    } else {

                    }
                } catch (e: HttpException) {

                } catch (e: Throwable) {
                    Log.e(
                        this@DashboardWearFragment.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }

        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                SpaceXInterface.create().getSingleLaunch("latest")
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        latest = response.await().body()
                        setLatest()
                    } else {

                    }
                } catch (e: HttpException) {

                } catch (e: Throwable) {
                    Log.e(
                        this@DashboardWearFragment.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setCountdown()
    }

    override fun onPause() {
        super.onPause()
        countdown?.cancel()
    }

    private fun setData() {
        launch?.let { model ->
            dashboard_name_text.text = model.missionName
            dashboard_date_text.text = launch?.tbd?.let {
                model.launchDateUnix.format(it)
            } ?: model.launchDateUnix.format()
            dashboard_flight_text.text =
                context?.getString(R.string.flight_number, model.flightNumber)
        }
    }

    private fun setLatest() {
        latest?.let { model ->
            dashboard_latest_name_text.text = model.missionName
            dashboard_latest_date_text.text = launch?.tbd?.let {
                model.launchDateUnix.format(it)
            } ?: model.launchDateUnix.format()
            dashboard_latest_flight_text.text =
                context?.getString(R.string.flight_number, model.flightNumber)
        }
    }

    private fun setCountdown() {
        launch?.let {
            val time = it.launchDateUnix.times(1000) - System.currentTimeMillis()

            countdown = object : CountDownTimer(time, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val format = String.format(
                        "%02d:%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toDays(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) -
                                TimeUnit.DAYS.toHours(
                                    TimeUnit.MILLISECONDS.toDays(
                                        millisUntilFinished
                                    )
                                ),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(
                                        millisUntilFinished
                                    )
                                ),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(
                                        millisUntilFinished
                                    )
                                )
                    )
                    dashboard_countdown_text.text = format
                }

                override fun onFinish() {
                    dashboard_countdown_text.text = "Finished"
                }
            }.start()
        }
    }
}
