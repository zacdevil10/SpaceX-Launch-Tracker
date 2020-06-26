package uk.co.zac_h.spacex.statistics

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
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
import uk.co.zac_h.spacex.model.spacex.LandingPadModel
import uk.co.zac_h.spacex.model.spacex.LaunchpadModel
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
    val mLandingPadModel: LandingPadModel = mock(LandingPadModel::class.java)
    @Mock
    val mLaunchpadModel: LaunchpadModel = mock(LaunchpadModel::class.java)

    private lateinit var launchpadList: List<LaunchpadModel>
    private lateinit var landingPadList: List<LandingPadModel>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = PadStatsInteractorImpl()
        mPresenter = PadStatsPresenterImpl(mView, mInteractor)
        presenter = PadStatsPresenterImpl(mView, interactor)

        launchpadList = listOf(mLaunchpadModel)
        landingPadList = listOf(mLandingPadModel)

        `when`(mLaunchpadModel.nameLong).thenReturn("")
        `when`(mLaunchpadModel.launchAttempts).thenReturn(0)
        `when`(mLaunchpadModel.launchSuccesses).thenReturn(0)
        `when`(mLaunchpadModel.status).thenReturn("")

        `when`(mLandingPadModel.nameFull).thenReturn("")
        `when`(mLandingPadModel.landingAttempts).thenReturn(0)
        `when`(mLandingPadModel.landingSuccesses).thenReturn(0)
        `when`(mLandingPadModel.status).thenReturn("")
        `when`(mLandingPadModel.type).thenReturn("")
    }

    @Test
    fun `When get pads then add to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getQueriedLandingPads()
            } doReturn Calls.response(Response.success(landingPadList))

            onBlocking {
                getQueriedLaunchpads()
            } doReturn Calls.response(Response.success(launchpadList))
        }

        presenter.getPads(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
        }
    }

    /*@Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getLandingPads()
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )

            onBlocking {
                getLaunchpads()
            } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        interactor.getPads(api = mockRepo, listener = mListener)

        verifyBlocking(mListener) {
            onError("Error: 404")
        }
    }*/

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getQueriedLaunchpads()
            } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
            onBlocking {
                getQueriedLandingPads()
            } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        presenter.getPads(mockRepo)

        verifyBlocking(mView) {
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