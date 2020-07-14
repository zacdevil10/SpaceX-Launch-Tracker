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
import uk.co.zac_h.spacex.vehicles.cores.CoreInteractorImpl
import uk.co.zac_h.spacex.vehicles.cores.CorePresenterImpl

class CoreTest {

    private lateinit var mPresenter: VehiclesContract.Presenter
    private lateinit var presenter: VehiclesContract.Presenter
    private lateinit var interactor: VehiclesContract.Interactor<CoreExtendedModel>

    @Mock
    val mInteractor: VehiclesContract.Interactor<CoreExtendedModel> = mock()

    @Mock
    val mView: VehiclesContract.View<CoreExtendedModel> = mock()

    @Mock
    val mListener: VehiclesContract.InteractorCallback<CoreExtendedModel> = mock()

    @Mock
    val mCoreModel: CoreExtendedModel = mock(CoreExtendedModel::class.java)

    private val mCoreDocsModel = CoreDocsModel(listOf(mCoreModel))

    private lateinit var query: QueryModel

    inline fun <reified T : Any> mock(): T = mock(T::class.java)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = CoreInteractorImpl()
        mPresenter = CorePresenterImpl(mView, mInteractor)
        presenter = CorePresenterImpl(mView, interactor)

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
    fun `When response from API is successful then add cores to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getCores(query) } doReturn Calls.response(Response.success(mCoreDocsModel))
        }

        presenter.getVehicles(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            toggleSwipeRefresh(false)
            updateVehicles(mCoreDocsModel.docs)
        }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getCores(query)
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        interactor.getVehicles(api = mockRepo, listener = mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getCores(query)
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        presenter.getVehicles(mockRepo)

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