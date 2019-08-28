package uk.co.zac_h.spacex.statistics.graphs.launchrate

import com.github.mikephil.charting.data.BarEntry
import uk.co.zac_h.spacex.utils.data.LaunchesModel

class LaunchRatePresenterImpl(
    private val view: LaunchRateView,
    private val interactor: LaunchRateInteractor
) : LaunchRatePresenter, LaunchRateInteractor.InteractorCallback {

    private lateinit var launchesList: List<LaunchesModel>

    private var filterFalconOne = true
    private var filterFalconNine = true
    private var filterFalconHeavy = true

    override fun getLaunchList() {
        if (!::launchesList.isInitialized) {
            view.showProgress()
            interactor.getLaunches(this)
        } else {
            onSuccess(launchesList)
        }
    }

    override fun updateFilter(id: String, isChecked: Boolean) {
        when (id) {
            "falcon1" -> filterFalconOne = isChecked
            "falcon9" -> filterFalconNine = isChecked
            "falconheavy" -> filterFalconHeavy = isChecked
        }
        if (::launchesList.isInitialized && launchesList.isNotEmpty()) onSuccess(launchesList)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(launches: List<LaunchesModel>?) {
        launches?.let {
            if (!::launchesList.isInitialized) launchesList = launches

            val entries = ArrayList<BarEntry>()

            val dataMap = LinkedHashMap<Int, Int>()
            val dataMapFuture = LinkedHashMap<Int, Int>()

            for (i in 2006..launches[launches.size - 1].launchYear) {
                dataMap[i + 1] = 0
            }

            launches.forEach {
                if (!filterFalconOne && it.rocket.id == "falcon1") return@forEach
                if (!filterFalconNine && it.rocket.id == "falcon9") return@forEach
                if (!filterFalconHeavy && it.rocket.id == "falconheavy") return@forEach

                if (it.launchDateUnix.times(1000) <= System.currentTimeMillis()) {
                    dataMap[it.launchYear + 1] = dataMap[it.launchYear + 1]?.plus(1) ?: 1
                } else {
                    dataMapFuture[it.launchYear + 1] =
                        dataMapFuture[it.launchYear + 1]?.plus(1) ?: 1
                }
            }

            dataMap.forEach {
                entries.add(
                    BarEntry(
                        it.key.toFloat(),
                        floatArrayOf(it.value.toFloat(), dataMapFuture[it.key]?.toFloat() ?: 0f)
                    )
                )
            }

            view.hideProgress()
            view.updateBarChart(entries, dataMap.size + 1)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}