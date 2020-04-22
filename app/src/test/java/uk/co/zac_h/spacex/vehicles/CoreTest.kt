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
import uk.co.zac_h.spacex.model.spacex.CoreModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.cores.CoreContract
import uk.co.zac_h.spacex.vehicles.cores.CoreInteractorImpl
import uk.co.zac_h.spacex.vehicles.cores.CorePresenterImpl

class CoreTest {

    private lateinit var mPresenter: CoreContract.CorePresenter
    private lateinit var presenter: CoreContract.CorePresenter
    private lateinit var interactor: CoreContract.CoreInteractor
    @Mock
    val mInteractor: CoreContract.CoreInteractor = mock(CoreContract.CoreInteractor::class.java)
    @Mock
    val mView: CoreContract.CoreView = mock(CoreContract.CoreView::class.java)
    @Mock
    val mListener: CoreContract.InteractorCallback =
        mock(CoreContract.InteractorCallback::class.java)
    @Mock
    val mCoreModel: CoreModel = mock(CoreModel::class.java)

    private lateinit var coreList: List<CoreModel>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = CoreInteractorImpl()
        mPresenter = CorePresenterImpl(mView, mInteractor)
        presenter = CorePresenterImpl(mView, interactor)

        coreList = listOf(mCoreModel)
    }

    @Test
    fun `When response from API is successful then add cores to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getCores() } doReturn Calls.response(Response.success(coreList))
        }

        presenter.getCores(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            toggleSwipeRefresh(false)
            updateCores(coreList)
        }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getCores()
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        interactor.getCores(api = mockRepo, listener = mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getCores()
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        presenter.getCores(mockRepo)

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