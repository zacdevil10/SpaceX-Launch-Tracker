package uk.co.zac_h.spacex.launches

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
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
import uk.co.zac_h.spacex.launches.details.ships.LaunchDetailsShipsContract
import uk.co.zac_h.spacex.launches.details.ships.LaunchDetailsShipsInteractor
import uk.co.zac_h.spacex.launches.details.ships.LaunchDetailsShipsPresenter
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface

class LaunchDetailsShipsTest {

    private lateinit var mPresenter: LaunchDetailsShipsContract.Presenter
    private lateinit var presenter: LaunchDetailsShipsContract.Presenter
    private lateinit var interactor: LaunchDetailsShipsContract.Interactor

    @Mock
    val mInteractor: LaunchDetailsShipsContract.Interactor =
        Mockito.mock(LaunchDetailsShipsContract.Interactor::class.java)

    @Mock
    val mView: LaunchDetailsShipsContract.View =
        Mockito.mock(LaunchDetailsShipsContract.View::class.java)

    @Mock
    val mListener: LaunchDetailsShipsContract.InteractorCallback =
        Mockito.mock(LaunchDetailsShipsContract.InteractorCallback::class.java)

    @Mock
    val mLaunchesExtendedModel: LaunchesExtendedModel =
        Mockito.mock(LaunchesExtendedModel::class.java)

    private val mLaunchesExtendedDocsModel: LaunchesExtendedDocsModel =
        LaunchesExtendedDocsModel(listOf(mLaunchesExtendedModel))

    private lateinit var query: QueryModel

    private lateinit var mockRepoSuccess: SpaceXInterface
    private lateinit var mockRepoError: SpaceXInterface

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = LaunchDetailsShipsInteractor()
        mPresenter = LaunchDetailsShipsPresenter(mView, mInteractor)
        presenter = LaunchDetailsShipsPresenter(mView, interactor)

        query = QueryModel(
            QueryLaunchesQueryModel("id"),
            QueryOptionsModel(
                false,
                listOf(
                    QueryPopulateModel(
                        "ships",
                        populate = listOf(
                            QueryPopulateModel(
                                "launches",
                                populate = "",
                                select = listOf("name", "flight_number")
                            )
                        ),
                        select = ""
                    )
                ), "", listOf("ships"), 1
            )
        )

        mockRepoSuccess = mock {
            onBlocking { getQueriedLaunches(query) } doReturn Calls.response(
                Response.success(
                    mLaunchesExtendedDocsModel
                )
            )
        }

        mockRepoError = mock {
            onBlocking { getQueriedLaunches(query) } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }
    }

    @Test
    fun `When launch id is provided then get launch cores data`() {
        interactor.getShips("id", mockRepoSuccess, mListener)

        verifyBlocking(mListener) {
            onSuccess(mLaunchesExtendedDocsModel)
        }
    }

    @Test
    fun `Add launch data to view`() {
        presenter.getShips("id", mockRepoSuccess)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            mLaunchesExtendedDocsModel.docs[0].ships?.let {
                updateShipsRecyclerView(it)
            }
        }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        interactor.getShips("id", mockRepoError, mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        presenter.getShips("id", mockRepoError)

        verifyBlocking(mView) {
            showProgress()
            showError("Error: 404")
        }
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequest()

        Mockito.verify(mInteractor).cancelRequest()
    }
}