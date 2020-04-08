package uk.co.zac_h.spacex.vehicles

import com.nhaarman.mockitokotlin2.*
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
import retrofit2.mock.Calls
import uk.co.zac_h.spacex.model.spacex.CoreModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.cores.details.CoreDetailsContract
import uk.co.zac_h.spacex.vehicles.cores.details.CoreDetailsInteractorImpl
import uk.co.zac_h.spacex.vehicles.cores.details.CoreDetailsPresenterImpl

class CoreDetailsTest {

    private lateinit var mPresenter: CoreDetailsContract.CoreDetailsPresenter
    private lateinit var presenter: CoreDetailsContract.CoreDetailsPresenter
    private lateinit var interactor: CoreDetailsContract.CoreDetailsInteractor

    @Mock
    val mInteractor: CoreDetailsContract.CoreDetailsInteractor =
        mock(CoreDetailsContract.CoreDetailsInteractor::class.java)

    @Mock
    val mView: CoreDetailsContract.CoreDetailsView =
        mock(CoreDetailsContract.CoreDetailsView::class.java)

    @Mock
    val mListener: CoreDetailsContract.InteractorCallback =
        mock(CoreDetailsContract.InteractorCallback::class.java)

    @Mock
    val mCoreModel: CoreModel = mock(CoreModel::class.java)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = CoreDetailsInteractorImpl()
        mPresenter = CoreDetailsPresenterImpl(mView, mInteractor)
        presenter = CoreDetailsPresenterImpl(mView, interactor)
    }

    @Test
    fun `When response from API is successful then add core to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getSingleCore("test") } doReturn Calls.response(Response.success(mCoreModel))
        }

        presenter.getCoreDetails("test", mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            updateCoreDetails(mCoreModel)
        }
    }

    @Test
    fun `When add core model then add to view`() {
        mPresenter.addCoreModel(mCoreModel)

        verify(mView).updateCoreDetails(mCoreModel)
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getSingleCore("")
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        interactor.getCoreDetails("", api = mockRepo, listener = mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getSingleCore("")
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        presenter.getCoreDetails("", mockRepo)

        verifyBlocking(mView) {
            showProgress()
            showError("Error: 404")
        }
    }

    @Test
    fun `When HttpException occurs`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getSingleCore("")
            } doThrow HttpException(
                Response.error<Any>(
                    500,
                    "Test server error".toResponseBody("text/plain".toMediaTypeOrNull())
                )
            )
        }

        interactor.getCoreDetails("", api = mockRepo, listener = mListener)

        verifyBlocking(mListener) { onError("HTTP 500 Response.error()") }
    }

    @Test(expected = Throwable::class)
    fun `When job fails to execute`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getSingleCore("")
            } doThrow Throwable()
        }

        interactor.getCoreDetails("", api = mockRepo, listener = mListener)
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequest()

        Mockito.verify(mInteractor).cancelAllRequests()
    }

}