package uk.co.zac_h.spacex.dashboard

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Response
import retrofit2.mock.Calls
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface

class DashboardTest {

    private lateinit var mPresenter: DashboardContract.DashboardPresenter
    private lateinit var presenter: DashboardContract.DashboardPresenter
    private lateinit var interactor: DashboardContract.DashboardInteractor

    @Mock
    val mInteractor: DashboardContract.DashboardInteractor =
        mock(DashboardContract.DashboardInteractor::class.java)

    @Mock
    val mView: DashboardContract.DashboardView = mock(DashboardContract.DashboardView::class.java)

    @Mock
    val mListener: DashboardContract.InteractorCallback =
        mock(DashboardContract.InteractorCallback::class.java)

    @Mock
    val mLaunchModel: LaunchesExtendedModel = mock(LaunchesExtendedModel::class.java)

    private val mLaunchExtendedDocsModel = LaunchesExtendedDocsModel(listOf(mLaunchModel))

    private val prefsMap: MutableMap<String, Any>? = mutableMapOf()

    private lateinit var queryNext: QueryModel
    private lateinit var queryLatest: QueryModel
    private lateinit var querySingle: QueryModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = DashboardInteractorImpl()
        mPresenter = DashboardPresenterImpl(mView, mInteractor)
        presenter = DashboardPresenterImpl(mView, interactor)

        prefsMap?.set("1", true)

        val populateList = listOf(
            QueryPopulateModel("rocket", populate = "", select = listOf("name")),
            QueryPopulateModel("crew", populate = "", select = listOf("id")),
            QueryPopulateModel("ships", populate = "", select = listOf("id"))
        )

        queryNext = QueryModel(
            QueryUpcomingLaunchesModel(true),
            QueryOptionsModel(
                true,
                populateList,
                QueryLaunchesSortModel("asc"),
                select = listOf(
                    "flight_number",
                    "name",
                    "date_unix",
                    "tbd",
                    "links.patch.small",
                    "rocket",
                    "crew",
                    "ships"
                ),
                limit = 1
            )
        )

        queryLatest = QueryModel(
            QueryUpcomingLaunchesModel(false),
            QueryOptionsModel(
                true,
                populateList,
                QueryLaunchesSortModel("desc"),
                select = listOf(
                    "flight_number",
                    "name",
                    "date_unix",
                    "tbd",
                    "links.patch.small",
                    "rocket",
                    "crew",
                    "ships"
                ),
                limit = 1
            )
        )

        querySingle = QueryModel(
            QueryLaunchesQueryModel("1"),
            QueryOptionsModel(
                true,
                populateList,
                "",
                select = listOf(
                    "flight_number",
                    "name",
                    "date_unix",
                    "tbd",
                    "links.patch.small",
                    "rocket",
                    "crew",
                    "ships"
                ),
                limit = 1
            )
        )
    }

    @Test
    fun `When countdown is available format milliseconds`() {
        mPresenter.updateCountdown(1584793994000 - 1582300970115)

        verify(mView).updateCountdown("T-28:20:30:23")
    }

    /*@Test
    fun `When next and latest launch are requested then add to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getQueriedLaunches(queryNext) } doReturn Calls.response(
                Response.success(
                    mLaunchExtendedDocsModel
                )
            )
            onBlocking { getQueriedLaunches(queryLatest) } doReturn Calls.response(
                Response.success(
                    mLaunchExtendedDocsModel
                )
            )
        }

        presenter.getLatestLaunches(api = mockRepo)

        verifyBlocking(mView) {
            updateNextLaunch(mLaunchModel)
            updateLatestLaunch(mLaunchModel)
        }

        verifyBlocking(mView, times(2)) { toggleSwipeProgress(false) }
    }*/

    @Test
    fun `When next and latest launches are not null then add to view`() {
        mPresenter.getLatestLaunches(mLaunchModel, mLaunchModel)

        verify(mView).apply {
            updateNextLaunch(mLaunchModel)
            updateLatestLaunch(mLaunchModel)
        }
    }

    @Test
    fun `When next launch is added and now date is set then hide countdown`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getQueriedLaunches(queryNext) } doReturn Calls.response(
                Response.success(
                    mLaunchExtendedDocsModel
                )
            )
            onBlocking { getQueriedLaunches(queryLatest) } doReturn Calls.response(
                Response.success(
                    mLaunchExtendedDocsModel
                )
            )
        }

        `when`(mLaunchModel.tbd).thenReturn(true)

        presenter.getLatestLaunches(api = mockRepo)

        verifyBlocking(mView) { hideCountdown() }
    }

    @Test
    fun `When pinned launch is added then add to pinned view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getQueriedLaunches(querySingle) } doReturn Calls.response(
                Response.success(
                    mLaunchExtendedDocsModel
                )
            )
        }

        presenter.getSingleLaunch("1", api = mockRepo)

        verifyBlocking(mView) { hidePinnedMessage() }
    }

    @Test
    fun `When response from API fails then show error`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getQueriedLaunches(queryNext) } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )

            onBlocking { getQueriedLaunches(queryLatest) } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        presenter.getLatestLaunches(api = mockRepo)

        verifyBlocking(mView) {
            toggleNextProgress(true)
            toggleLatestProgress(true)
            showError("Error: 404")
            showError("Error: 404")
            toggleSwipeProgress(false)
        }
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequests()

        verify(mInteractor).cancelAllRequests()
    }

    @Test
    fun `Show next launch`() {
        mPresenter.toggleNextLaunchVisibility(true)

        verify(mView).showNextLaunch()
    }

    @Test
    fun `Hide next launch`() {
        mPresenter.toggleNextLaunchVisibility(false)

        verify(mView).hideNextLaunch()
    }

    @Test
    fun `Show latest launch`() {
        mPresenter.toggleLatestLaunchVisibility(true)

        verify(mView).showLatestLaunch()
    }

    @Test
    fun `Hide latest launch`() {
        mPresenter.toggleLatestLaunchVisibility(false)

        verify(mView).hideLatestLaunch()
    }

    @Test
    fun `Show pinned launch list`() {
        mPresenter.togglePinnedList(true)

        verify(mView).showPinnedList()
    }

    @Test
    fun `Hide pinned launch list`() {
        mPresenter.togglePinnedList(false)

        verify(mView).hidePinnedList()
    }
}