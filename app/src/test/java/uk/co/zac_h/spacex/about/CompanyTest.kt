package uk.co.zac_h.spacex.about

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyBlocking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import uk.co.zac_h.spacex.about.company.*
import uk.co.zac_h.spacex.model.spacex.CompanyModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

@ExperimentalCoroutinesApi
class CompanyTest {

    private lateinit var mPresenter: CompanyPresenter
    private lateinit var presenter: CompanyPresenter
    private lateinit var interactor: CompanyInteractor
    @Mock
    val mInteractor: CompanyInteractor = mock(CompanyInteractor::class.java)
    @Mock
    val mView: CompanyView = mock(CompanyView::class.java)
    @Mock
    val mListener: CompanyInteractor.Callback = mock(CompanyInteractor.Callback::class.java)
    @Mock
    val mCompanyModel: CompanyModel = mock(CompanyModel::class.java)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = CompanyInteractorImpl(Dispatchers.Unconfined)
        mPresenter = CompanyPresenterImpl(mView, mInteractor)
        presenter = CompanyPresenterImpl(mView, interactor)
    }

    @Test
    fun `When mCompanyModel is not null then add to view`() {
        mPresenter.getCompanyInfo(mCompanyModel)

        verify(mView).hideProgress()
        verify(mView).updateCompanyInfo(mCompanyModel)
    }

    @Test
    fun `When mCompanyModel is null then get data from API`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getCompanyInfo() } doReturn Response.success(mCompanyModel)
        }

        interactor.getCompanyInfo(mockRepo, mListener)

        verifyBlocking(mListener) { onSuccess(mCompanyModel) }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getCompanyInfo() } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        interactor.getCompanyInfo(mockRepo, mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getCompanyInfo() } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        presenter.getCompanyInfo(null, mockRepo)

        verifyBlocking(mView) { showProgress() }
        verifyBlocking(mView) { showError("Error: 404") }
    }

    @Test
    fun `When HttpException occurs`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getCompanyInfo() } doThrow HttpException(
                Response.error<Any>(
                    500,
                    "Test server error".toResponseBody("text/plain".toMediaTypeOrNull())
                )
            )
        }

        interactor.getCompanyInfo(mockRepo, mListener)

        verifyBlocking(mListener) { onError("HTTP 500 Response.error()") }
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequest()

        verify(mInteractor).cancelAllRequests()
    }
}