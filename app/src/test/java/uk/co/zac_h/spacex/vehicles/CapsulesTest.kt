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
import uk.co.zac_h.spacex.model.spacex.CapsulesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.capsules.*

class CapsulesTest {

    private lateinit var mPresenter: CapsulesPresenter
    private lateinit var presenter: CapsulesPresenter
    private lateinit var interactor: CapsulesInteractor
    @Mock
    val mInteractor: CapsulesInteractor = mock(CapsulesInteractor::class.java)
    @Mock
    val mView: CapsulesView = mock(CapsulesView::class.java)
    @Mock
    val mListener: CapsulesInteractor.Callback = mock(CapsulesInteractor.Callback::class.java)
    @Mock
    val mCapsulesModel: CapsulesModel = mock(CapsulesModel::class.java)

    private lateinit var capsulesList: List<CapsulesModel>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = CapsulesInteractorImpl(Dispatchers.Unconfined)
        mPresenter = CapsulesPresenterImpl(mView, mInteractor)
        presenter = CapsulesPresenterImpl(mView, interactor)

        capsulesList = listOf(mCapsulesModel)
    }

    @Test
    fun `When response from API is successful then add to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getCapsules() } doReturn Response.success(capsulesList)
        }

        presenter.getCapsules(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            toggleSwipeRefresh(false)
            updateCapsules(capsulesList)
        }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getCapsules()
            } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        interactor.getCapsules(api = mockRepo, listener = mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getCapsules()
            } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        presenter.getCapsules(mockRepo)

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
                getCapsules()
            } doThrow HttpException(
                Response.error<Any>(
                    500,
                    "Test server error".toResponseBody("text/plain".toMediaTypeOrNull())
                )
            )
        }

        interactor.getCapsules(api = mockRepo, listener = mListener)

        verifyBlocking(mListener) { onError("HTTP 500 Response.error()") }
    }

    @Test(expected = Throwable::class)
    fun `When job fails to execute`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getCapsules()
            } doThrow Throwable()
        }

        interactor.getCapsules(api = mockRepo, listener = mListener)
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequests()

        Mockito.verify(mInteractor).cancelAllRequests()
    }

}