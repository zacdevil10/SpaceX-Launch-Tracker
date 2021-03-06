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
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface

class LaunchesTest {

    private lateinit var mPresenter: LaunchesContract.LaunchesPresenter
    private lateinit var presenter: LaunchesContract.LaunchesPresenter
    private lateinit var interactor: LaunchesContract.LaunchesInteractor

    @Mock
    val mInteractor: LaunchesContract.LaunchesInteractor =
        mock(LaunchesContract.LaunchesInteractor::class.java)

    @Mock
    val mView: LaunchesContract.LaunchesView = mock(LaunchesContract.LaunchesView::class.java)

    @Mock
    val mListener: LaunchesContract.InteractorCallback =
        mock(LaunchesContract.InteractorCallback::class.java)

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

        interactor = LaunchesInteractorImpl()
        mPresenter = LaunchesPresenterImpl(mView, mInteractor)
        presenter = LaunchesPresenterImpl(mView, interactor)

        query = QueryModel(
            query = QueryUpcomingLaunchesModel(true),
            options = QueryOptionsModel(
                pagination = false,
                populate = listOf(
                    QueryPopulateModel(path = "rocket", populate = "", select = listOf("name")),
                    QueryPopulateModel("launchpad", select = listOf("name"), populate = ""),
                    QueryPopulateModel("crew", populate = "", select = listOf("id")),
                    QueryPopulateModel("ships", populate = "", select = listOf("id")),
                    QueryPopulateModel(
                        path = "cores",
                        populate = listOf(
                            QueryPopulateModel(
                                path = "landpad",
                                populate = "",
                                select = listOf("name")
                            ),
                            QueryPopulateModel(
                                path = "core",
                                populate = "",
                                select = listOf("reuse_count")
                            )
                        ),
                        select = ""
                    )
                ),
                sort = QueryLaunchesSortModel("asc"),
                select = listOf(
                    "flight_number",
                    "name",
                    "date_unix",
                    "tbd",
                    "links.patch.small",
                    "rocket",
                    "cores.core",
                    "cores.reused",
                    "cores.landpad",
                    "crew",
                    "ships",
                    "links",
                    "static_fire_date_unix",
                    "details",
                    "launchpad",
                    "date_precision"
                ),
                limit = 5000
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
    fun `When get future launches then add ascending to view`() {
        presenter.getLaunchList("upcoming", mockRepoSuccess)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            updateLaunchesList(mLaunchesExtendedDocsModel.docs)
        }
    }

    @Test
    fun `When get past launches then add descending to view`() {
        presenter.getLaunchList("upcoming", mockRepoSuccess)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            updateLaunchesList(mLaunchesExtendedDocsModel.docs)
        }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        interactor.getLaunches("upcoming", "asc", mockRepoError, mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        presenter.getLaunchList("upcoming", mockRepoError)

        verifyBlocking(mView) {
            showProgress()
            showError("Error: 404")
            toggleSwipeProgress(false)
        }
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequests()

        Mockito.verify(mInteractor).cancelAllRequests()
    }

}