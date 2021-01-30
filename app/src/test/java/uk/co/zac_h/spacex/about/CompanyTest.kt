package uk.co.zac_h.spacex.about

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.Response
import retrofit2.mock.Calls
import uk.co.zac_h.spacex.about.company.CompanyContract
import uk.co.zac_h.spacex.about.company.CompanyInteractorImpl
import uk.co.zac_h.spacex.about.company.CompanyPresenterImpl
import uk.co.zac_h.spacex.model.spacex.CompanyResponse
import uk.co.zac_h.spacex.rest.SpaceXInterface

class CompanyTest {

    private lateinit var mPresenter: CompanyContract.CompanyPresenter
    private lateinit var presenter: CompanyContract.CompanyPresenter
    private lateinit var interactor: CompanyContract.CompanyInteractor

    @Mock
    val mInteractor: CompanyContract.CompanyInteractor =
        mock(CompanyContract.CompanyInteractor::class.java)

    @Mock
    val mView: CompanyContract.CompanyView = mock(CompanyContract.CompanyView::class.java)

    @Mock
    val mListener: CompanyContract.InteractorCallback =
        mock(CompanyContract.InteractorCallback::class.java)

    @Mock
    val mCompany: CompanyResponse = mock(CompanyResponse::class.java)

    private lateinit var mockRepoError: SpaceXInterface

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = CompanyInteractorImpl()
        mPresenter = CompanyPresenterImpl(mView, mInteractor)
        presenter = CompanyPresenterImpl(mView, interactor)

        mockRepoError = mock {
            onBlocking { getCompanyInfo() } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }
    }

    @Test
    fun `When mCompanyModel is not null then add to view`() {
        mPresenter.getCompanyInfo(mCompany)

        verify(mView).hideProgress()
        verify(mView).updateCompanyInfo(mCompany)
    }

    @Test
    fun `When mCompanyModel is null then get data from API`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getCompanyInfo() } doReturn Calls.response(Response.success(mCompany))
        }

        interactor.getCompanyInfo(mockRepo, mListener)

        verifyBlocking(mListener) { onSuccess(mCompany) }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        interactor.getCompanyInfo(mockRepoError, mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        presenter.getCompanyInfo(null, mockRepoError)

        verifyBlocking(mView) { showProgress() }
        verifyBlocking(mView) { showError("Error: 404") }
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequest()

        verify(mInteractor).cancelAllRequests()
    }
}