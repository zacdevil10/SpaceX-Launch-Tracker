package uk.co.zac_h.spacex.vehicles

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Response
import retrofit2.mock.Calls
import uk.co.zac_h.spacex.model.spacex.RocketsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.rockets.RocketContract
import uk.co.zac_h.spacex.vehicles.rockets.RocketInteractorImpl
import uk.co.zac_h.spacex.vehicles.rockets.RocketPresenterImpl

class RocketTest {

    private lateinit var mPresenter: RocketContract.RocketPresenter
    private lateinit var presenter: RocketContract.RocketPresenter
    private lateinit var interactor: RocketContract.RocketInteractor
    @Mock
    val mInteractor: RocketContract.RocketInteractor =
        mock(RocketContract.RocketInteractor::class.java)
    @Mock
    val mView: RocketContract.RocketView = mock(RocketContract.RocketView::class.java)
    @Mock
    val mListener: RocketContract.InteractorCallback =
        mock(RocketContract.InteractorCallback::class.java)
    @Mock
    val mRocketsModel: RocketsModel = mock(RocketsModel::class.java)

    private lateinit var rocketsList: List<RocketsModel>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = RocketInteractorImpl()
        mPresenter = RocketPresenterImpl(mView, mInteractor)
        presenter = RocketPresenterImpl(mView, interactor)

        rocketsList = listOf(mRocketsModel)
    }

    @Test
    fun `When response from API is successful then add cores to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getRockets() } doReturn Calls.response(Response.success(rocketsList))
        }

        presenter.getRockets(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            toggleSwipeRefresh(false)
            updateRockets(rocketsList)
        }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getRockets()
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        interactor.getRockets(api = mockRepo, listener = mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getRockets()
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        presenter.getRockets(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            showError("Error: 404")
            toggleSwipeRefresh(false)
        }
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequest()

        Mockito.verify(mInteractor).cancelAllRequests()
    }

}