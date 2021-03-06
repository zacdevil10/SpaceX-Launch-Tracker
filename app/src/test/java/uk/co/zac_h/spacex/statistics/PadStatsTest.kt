package uk.co.zac_h.spacex.statistics

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verifyBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Response
import retrofit2.mock.Calls
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.statistics.graphs.padstats.PadStatsContract
import uk.co.zac_h.spacex.statistics.graphs.padstats.PadStatsInteractorImpl
import uk.co.zac_h.spacex.statistics.graphs.padstats.PadStatsPresenterImpl

class PadStatsTest {

    private lateinit var mPresenter: PadStatsContract.PadStatsPresenter
    private lateinit var presenter: PadStatsContract.PadStatsPresenter
    private lateinit var interactor: PadStatsContract.PadStatsInteractor
    @Mock
    val mInteractor: PadStatsContract.PadStatsInteractor =
        mock(PadStatsContract.PadStatsInteractor::class.java)
    @Mock
    val mView: PadStatsContract.PadStatsView = mock(PadStatsContract.PadStatsView::class.java)

    @Mock
    val mListener: PadStatsContract.InteractorCallback =
        mock(PadStatsContract.InteractorCallback::class.java)

    @Mock
    val mLandingPadModel: LandingPadQueriedResponse = mock(LandingPadQueriedResponse::class.java)

    @Mock
    val mLaunchpadModel: LaunchpadModel = mock(LaunchpadModel::class.java)

    private lateinit var launchQuery: QueryModel
    private lateinit var landQuery: QueryModel

    private var launchpadList = listOf(mLaunchpadModel)
    private var landingPadList = listOf(mLandingPadModel)

    private val launchpadDocsModel = LaunchpadDocsModel(launchpadList)
    private val landingPadDocsModel = LandingPadDocsModel(landingPadList)

    private lateinit var mockRepoError: SpaceXInterface

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = PadStatsInteractorImpl()
        mPresenter = PadStatsPresenterImpl(mView, mInteractor)
        presenter = PadStatsPresenterImpl(mView, interactor)

        `when`(mLaunchpadModel.fullName).thenReturn("")
        `when`(mLaunchpadModel.launchAttempts).thenReturn(0)
        `when`(mLaunchpadModel.launchSuccesses).thenReturn(0)
        `when`(mLaunchpadModel.status).thenReturn("")

        `when`(mLandingPadModel.fullName).thenReturn("")
        `when`(mLandingPadModel.landingAttempts).thenReturn(0)
        `when`(mLandingPadModel.landingSuccesses).thenReturn(0)
        `when`(mLandingPadModel.status).thenReturn("")
        `when`(mLandingPadModel.type).thenReturn("")

        launchQuery = QueryModel(
            "",
            QueryOptionsModel(
                false,
                "",
                "",
                listOf("full_name", "launch_attempts", "launch_successes", "status"),
                100000
            )
        )

        landQuery = QueryModel(
            "",
            QueryOptionsModel(
                false,
                "",
                "",
                listOf("full_name", "landing_attempts", "landing_successes", "status", "type"),
                100000
            )
        )

        mockRepoError = mock {
            onBlocking {
                getQueriedLandingPads(landQuery)
            } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )

            onBlocking {
                getQueriedLaunchpads(launchQuery)
            } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }
    }

    @Test
    fun `When get pads then add to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getQueriedLandingPads(landQuery)
            } doReturn Calls.response(Response.success(landingPadDocsModel))

            onBlocking {
                getQueriedLaunchpads(launchQuery)
            } doReturn Calls.response(Response.success(launchpadDocsModel))
        }

        presenter.getLaunchpads(mockRepo)
        presenter.getLandingPads(mockRepo)

        verifyBlocking(mView, times(2)) {
            showProgress()
            hideProgress()
        }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        interactor.getLaunchpads(api = mockRepoError, listener = mListener)
        interactor.getLandingPads(api = mockRepoError, listener = mListener)

        verifyBlocking(mListener, times(2)) {
            onError("Error: 404")
        }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        presenter.getLandingPads(mockRepoError)
        presenter.getLaunchpads(mockRepoError)

        verifyBlocking(mView, times(2)) {
            showProgress()
            showError("Error: 404")
        }
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequests()

        Mockito.verify(mInteractor).cancelAllRequests()
    }
}