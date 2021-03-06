package uk.co.zac_h.spacex.vehicles

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verifyBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import retrofit2.mock.Calls
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.ships.ShipsInteractorImpl
import uk.co.zac_h.spacex.vehicles.ships.ShipsPresenterImpl

class ShipsTest {

    private lateinit var mPresenter: VehiclesContract.Presenter
    private lateinit var presenter: VehiclesContract.Presenter
    private lateinit var interactor: VehiclesContract.Interactor<ShipExtendedModel>

    @Mock
    val mInteractor: VehiclesContract.Interactor<ShipExtendedModel> = mock()

    @Mock
    val mView: VehiclesContract.View<ShipExtendedModel> = mock()

    @Mock
    val mListener: VehiclesContract.InteractorCallback<ShipExtendedModel> = mock()

    @Mock
    val shipExtendedModel: ShipExtendedModel = Mockito.mock(ShipExtendedModel::class.java)

    private val shipDocsModel: ShipsDocsModel = ShipsDocsModel(listOf(shipExtendedModel))

    private lateinit var query: QueryModel

    inline fun <reified T : Any> mock(): T = Mockito.mock(T::class.java)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = ShipsInteractorImpl()
        mPresenter = ShipsPresenterImpl(mView, mInteractor)
        presenter = ShipsPresenterImpl(mView, interactor)

        query = QueryModel(
            query = "",
            options = QueryOptionsModel(
                false,
                populate = listOf(
                    QueryPopulateModel(
                        path = "launches",
                        select = listOf("flight_number", "name"),
                        populate = ""
                    )
                ),
                sort = "",
                select = "",
                limit = 200
            )
        )
    }

    @Test
    fun `When response from API is successful then add cores to view`() {
        val mockRepo = com.nhaarman.mockitokotlin2.mock<SpaceXInterface> {
            onBlocking { getShips(query) } doReturn Calls.response(Response.success(shipDocsModel))
        }

        presenter.getVehicles(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            toggleSwipeRefresh(false)
            updateVehicles(shipDocsModel.docs)
        }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = com.nhaarman.mockitokotlin2.mock<SpaceXInterface> {
            onBlocking {
                getShips(query)
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
        val mockRepo = com.nhaarman.mockitokotlin2.mock<SpaceXInterface> {
            onBlocking {
                getShips(query)
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