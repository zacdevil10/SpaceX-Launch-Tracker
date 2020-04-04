package uk.co.zac_h.spacex.statistics

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyBlocking
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import uk.co.zac_h.spacex.model.spacex.LandingPadModel
import uk.co.zac_h.spacex.model.spacex.LaunchpadModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.statistics.graphs.padstats.*

class PadStatsTest {

    private lateinit var mPresenter: PadStatsPresenter
    private lateinit var presenter: PadStatsPresenter
    private lateinit var interactor: PadStatsInteractor
    @Mock
    val mInteractor: PadStatsInteractor = mock(PadStatsInteractor::class.java)
    @Mock
    val mView: PadStatsView = mock(PadStatsView::class.java)
    @Mock
    val mListener: PadStatsInteractor.InteractorCallback =
        mock(PadStatsInteractor.InteractorCallback::class.java)

    @Mock
    val mLandingPadModel: LandingPadModel = mock(LandingPadModel::class.java)
    @Mock
    val mLaunchpadModel: LaunchpadModel = mock(LaunchpadModel::class.java)

    private lateinit var launchpadList: List<LaunchpadModel>
    private lateinit var landingPadList: List<LandingPadModel>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = PadStatsInteractorImpl(Dispatchers.Unconfined)
        mPresenter = PadStatsPresenterImpl(mView, mInteractor)
        presenter = PadStatsPresenterImpl(mView, interactor)

        launchpadList = listOf(mLaunchpadModel)
        landingPadList = listOf(mLandingPadModel)
    }

    @Test
    fun `When get pads then add to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getLandingPads()
            } doReturn Response.success(landingPadList)

            onBlocking {
                getLaunchpads()
            } doReturn Response.success(launchpadList)
        }

        presenter.getPads(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
        }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getLandingPads()
            } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        interactor.getPads(type = PadType.LANDING, api = mockRepo, listener = mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getLaunchpads()
                getLandingPads()
            } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        presenter.getPads(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            showError("Error: 404")
        }
    }

    @Test
    fun `When HttpException occurs`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getLaunchpads()
            } doThrow HttpException(
                Response.error<Any>(
                    500,
                    "Test server error".toResponseBody("text/plain".toMediaTypeOrNull())
                )
            )
        }

        interactor.getPads(listener = mListener)

        verifyBlocking(mListener) { onError("HTTP 500 Response.error()") }
    }

    @Test(expected = Throwable::class)
    fun `When job fails to execute`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getLaunchpads()
            } doThrow Throwable()
        }

        interactor.getPads(listener = mListener)
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequests()

        Mockito.verify(mInteractor).cancelAllRequests()
    }
}