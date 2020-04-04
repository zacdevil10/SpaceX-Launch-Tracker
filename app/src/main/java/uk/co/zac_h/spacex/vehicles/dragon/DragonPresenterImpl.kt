package uk.co.zac_h.spacex.vehicles.dragon

import uk.co.zac_h.spacex.model.spacex.DragonModel

class DragonPresenterImpl(private val view: DragonView, private val interactor: DragonInteractor) :
    DragonPresenter, DragonInteractor.Callback {

    override fun getDragon() {
        view.showProgress()
        interactor.getDragon(this)
    }

    override fun cancelRequest() {
        interactor.cancelRequest()
    }

    override fun onSuccess(dragon: List<DragonModel>?) {
        view.apply {
            hideProgress()
            toggleSwipeRefresh(false)
            dragon?.let { updateDragon(it.reversed()) }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }

}