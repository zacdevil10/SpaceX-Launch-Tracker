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
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Response
import retrofit2.mock.Calls
import uk.co.zac_h.spacex.launches.details.cores.LaunchDetailsCoresContract
import uk.co.zac_h.spacex.launches.details.cores.LaunchDetailsCoresInteractor
import uk.co.zac_h.spacex.launches.details.cores.LaunchDetailsCoresPresenter
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface

class LaunchDetailsCoresTest {

    private lateinit var mPresenter: LaunchDetailsCoresContract.Presenter
    private lateinit var presenter: LaunchDetailsCoresContract.Presenter
    private lateinit var interactor: LaunchDetailsCoresContract.Interactor

    @Mock
    val mInteractor: LaunchDetailsCoresContract.Interactor =
        mock(LaunchDetailsCoresContract.Interactor::class.java)

    @Mock
    val mView: LaunchDetailsCoresContract.View = mock(LaunchDetailsCoresContract.View::class.java)

    @Mock
    val mListener: LaunchDetailsCoresContract.InteractorCallback =
        mock(LaunchDetailsCoresContract.InteractorCallback::class.java)

    @Mock
    val mLaunchesExtendedModel: LaunchesExtendedModel = mock(LaunchesExtendedModel::class.java)

    private val mLaunchesExtendedDocsModel: LaunchesExtendedDocsModel =
        LaunchesExtendedDocsModel(listOf(mLaunchesExtendedModel))

    private lateinit var query: QueryModel

    private lateinit var mockRepoSuccess: SpaceXInterface
    private lateinit var mockRepoError: SpaceXInterface

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = LaunchDetailsCoresInteractor()
        mPresenter = LaunchDetailsCoresPresenter(mView, mInteractor)
        presenter = LaunchDetailsCoresPresenter(mView, interactor)

        val populateList = listOf(
            QueryPopulateModel(
                "cores",
                populate = listOf(
                    QueryPopulateModel(
                        "core",
                        populate = listOf(
                            QueryPopulateModel(
                                "launches",
                                select = listOf("name", "flight_number"),
                                populate = ""
                            )
                        ),
                        select = ""
                    ),
                    QueryPopulateModel(
                        "landpad",
                        select = "",
                        populate = ""
                    )
                ),
                select = ""
            )
        )

        query = QueryModel(
            QueryLaunchesQueryModel("id"),
            QueryOptionsModel(false, populateList, "", listOf("cores"), 10)
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
        interactor.getCores("id", mockRepoSuccess, mListener)

        verifyBlocking(mListener) {
            onSuccess(mLaunchesExtendedDocsModel)
        }
    }

    @Test
    fun `Add launch data to view`() {
        presenter.getLaunch("id", mockRepoSuccess)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            mLaunchesExtendedDocsModel.docs[0].cores?.let {
                updateCoresRecyclerView(it)
            }
        }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        interactor.getCores("id", mockRepoError, mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        presenter.getLaunch("id", mockRepoError)

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