package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.model.LaunchesModel

class LaunchesWearPresenterImpl(
    private val view: LaunchesWearView,
    private val interactor: LaunchesWearInteractor
) : LaunchesWearPresenter, LaunchesWearInteractor.Callback {

    override fun getLaunches(id: String) {
        view.apply {
            showProgress()
            hideReload()
        }
        interactor.getAllLaunches(id, if (id == "past") "desc" else "asc", this)
    }

    override fun cancelRequests() {
        interactor.cancelRequest()
    }

    override fun onSuccess(launches: List<LaunchesModel>?) {
        launches?.let {
            view.apply {
                updateLaunches(launches)
                hideProgress()
                hideReload()
            }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            showReload()
            hideProgress()
        }
    }
}