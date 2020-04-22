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
import uk.co.zac_h.spacex.model.spacex.CompanyModel
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
    val mCompanyModel: CompanyModel = mock(CompanyModel::class.java)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = CompanyInteractorImpl()
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
            onBlocking { getCompanyInfo() } doReturn Calls.response(Response.success(mCompanyModel))
        }

        interactor.getCompanyInfo(mockRepo, mListener)

        verifyBlocking(mListener) { onSuccess(mCompanyModel) }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getCompanyInfo() } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        interactor.getCompanyInfo(mockRepo, mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getCompanyInfo() } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        presenter.getCompanyInfo(null, mockRepo)

        verifyBlocking(mView) { showProgress() }
        verifyBlocking(mView) { showError("Error: 404") }
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequest()

        verify(mInteractor).cancelAllRequests()
    }
}