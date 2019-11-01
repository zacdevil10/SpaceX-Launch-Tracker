package uk.co.zac_h.spacex.statistics.graphs.launchrate

import uk.co.zac_h.spacex.model.LaunchesModel

class LaunchRatePresenterImpl(
    private val view: LaunchRateView,
    private val interactor: LaunchRateInteractor
) : LaunchRatePresenter, LaunchRateInteractor.InteractorCallback {

    private var launchesList = ArrayList<LaunchesModel>()

    override fun getLaunchList() {
        view.showProgress()
        interactor.getLaunches(this)
    }

    override fun addLaunchList(launches: ArrayList<LaunchesModel>) {
        onSuccess(launches, false)
    }

    override fun showFilter(filterVisible: Boolean) {
        view.showFilter(filterVisible)
    }

    override fun updateFilter(launches: ArrayList<LaunchesModel>, id: String, isChecked: Boolean) {
        view.apply {
            when (id) {
                "falcon1" -> setFalconOneFilter(isChecked)
                "falcon9" -> setFalconNineFilter(isChecked)
                "falconheavy" -> setFalconHeavyFilter(isChecked)
            }
        }
        if (launchesList.isNotEmpty()) onSuccess(launches, false)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(launches: List<LaunchesModel>?, animate: Boolean) {
        launches?.let {
            launchesList.clear()
            launchesList.addAll(launches)

            view.apply {
                hideProgress()
                updateBarChart(launchesList, animate)
            }
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}