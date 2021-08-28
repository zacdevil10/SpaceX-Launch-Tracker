package uk.co.zac_h.spacex.launches.details.details

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Launch
import uk.co.zac_h.spacex.retrofit.NetworkModule
import uk.co.zac_h.spacex.retrofit.SpaceXService
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper

class LaunchDetailsPresenterImpl(
    private val view: NetworkInterface.View<Launch>,
    private val helper: PinnedSharedPreferencesHelper,
    private val interactor: NetworkInterface.Interactor<Launch>
) : LaunchDetailsContract.LaunchDetailsPresenter,
    NetworkInterface.Callback<Launch> {

    override fun get(data: Any, api: SpaceXService) {
        view.showProgress()
        interactor.get(data, NetworkModule.providesSpaceXHttpClientV5(), this)
    }

    override fun getOrUpdate(response: Launch?, data: Any, api: SpaceXService) {
        response?.let { onSuccess(response) } ?: super.getOrUpdate(response, data, api)
    }

    override fun pinLaunch(id: String, pin: Boolean) {
        helper.setPinnedLaunch(id, pin)
    }

    override fun isPinned(id: String?): Boolean = id?.let { helper.isPinned(it) } ?: false

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(response: Launch) {
        view.apply {
            update(response)
            hideProgress()
            toggleSwipeRefresh(false)
        }
    }

    override fun onError(error: String) {
        view.apply {
            toggleSwipeRefresh(false)
            showError(error)
        }
    }
}