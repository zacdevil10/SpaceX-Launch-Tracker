package uk.co.zac_h.spacex.vehicles

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyBlocking
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import uk.co.zac_h.spacex.model.spacex.RocketsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.rockets.*

class RocketTest {

    private lateinit var mPresenter: RocketPresenter
    private lateinit var presenter: RocketPresenter
    private lateinit var interactor: RocketInteractor
    @Mock
    val mInteractor: RocketInteractor = mock(RocketInteractor::class.java)
    @Mock
    val mView: RocketView = mock(RocketView::class.java)
    @Mock
    val mListener: RocketInteractor.Callback = mock(RocketInteractor.Callback::class.java)
    @Mock
    val mRocketsModel: RocketsModel = mock(RocketsModel::class.java)

    private lateinit var rocketsList: List<RocketsModel>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = RocketInteractorImpl(Dispatchers.Unconfined)
        mPresenter = RocketPresenterImpl(mView, mInteractor)
        presenter = RocketPresenterImpl(mView, interactor)

        rocketsList = listOf(mRocketsModel)
    }

    @Test
    fun `When response from API is successful then add cores to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getRockets() } doReturn Response.success(rocketsList)
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
            } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
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
            } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
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
    fun `When HttpException occurs`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getRockets()
            } doThrow HttpException(
                Response.error<Any>(
                    500,
                    "Test server error".toResponseBody("text/plain".toMediaTypeOrNull())
                )
            )
        }

        interactor.getRockets(api = mockRepo, listener = mListener)

        verifyBlocking(mListener) { onError("HTTP 500 Response.error()") }
    }

    @Test(expected = Throwable::class)
    fun `When job fails to execute`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getRockets()
            } doThrow Throwable()
        }

        interactor.getRockets(api = mockRepo, listener = mListener)
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequest()

        Mockito.verify(mInteractor).cancelAllRequests()
    }

}