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
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.capsules.CapsulesContract
import uk.co.zac_h.spacex.vehicles.capsules.CapsulesInteractorImpl
import uk.co.zac_h.spacex.vehicles.capsules.CapsulesPresenterImpl

class CapsulesTest {

    private lateinit var mPresenter: CapsulesContract.CapsulesPresenter
    private lateinit var presenter: CapsulesContract.CapsulesPresenter
    private lateinit var interactor: CapsulesContract.CapsulesInteractor

    @Mock
    val mInteractor: CapsulesContract.CapsulesInteractor =
        mock(CapsulesContract.CapsulesInteractor::class.java)

    @Mock
    val mView: CapsulesContract.CapsulesView = mock(CapsulesContract.CapsulesView::class.java)

    @Mock
    val mListener: CapsulesContract.InteractorCallback =
        mock(CapsulesContract.InteractorCallback::class.java)

    @Mock
    val mCapsulesModel: CapsulesModel = mock(CapsulesModel::class.java)

    private var capsulesList = listOf(mCapsulesModel)

    private val capsulesDocsMock = CapsulesDocsModel(capsulesList)

    private lateinit var query: QueryModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = CapsulesInteractorImpl()
        mPresenter = CapsulesPresenterImpl(mView, mInteractor)
        presenter = CapsulesPresenterImpl(mView, interactor)

        val populateList: ArrayList<QueryPopulateModel> = ArrayList()

        populateList.add(
            QueryPopulateModel(
                "launches",
                select = listOf("name", "flight_number"),
                populate = ""
            )
        )

        query = QueryModel("", QueryOptionsModel(false, populateList, "", "", 100000))
    }

    @Test
    fun `When response from API is successful then add to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getCapsules(query) } doReturn Calls.response(
                Response.success(
                    capsulesDocsMock
                )
            )
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
                getCapsules(query)
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        interactor.getCapsules(api = mockRepo, listener = mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getCapsules(query)
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
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
    fun `Cancel request`() {
        mPresenter.cancelRequests()

        Mockito.verify(mInteractor).cancelAllRequests()
    }

}