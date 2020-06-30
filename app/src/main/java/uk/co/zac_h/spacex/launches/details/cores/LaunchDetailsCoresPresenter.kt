package uk.co.zac_h.spacex.launches.details.cores

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedDocsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

class LaunchDetailsCoresPresenter(
    private val view: LaunchDetailsCoresContract.View,
    private val interactor: LaunchDetailsCoresContract.Interactor
) : LaunchDetailsCoresContract.Presenter, LaunchDetailsCoresContract.InteractorCallback {

    override fun getLaunch(id: String, api: SpaceXInterface) {
        view.showProgress()
        interactor.getSingleLaunch(id, api, this)
    }

    override fun cancelRequest() {
        interactor.cancelRequest()
    }

    override fun onSuccess(launchModel: LaunchesExtendedDocsModel?) {
        if (launchModel?.docs?.isNotEmpty() == true) launchModel.docs[0].cores?.let {
            view.updateCoresRecyclerView(it)
        }
        view.hideProgress()
    }

    override fun onError(error: String) {
        view.showProgress()
    }
}