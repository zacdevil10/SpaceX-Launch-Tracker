package uk.co.zac_h.spacex.statistics

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
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
import uk.co.zac_h.spacex.model.spacex.LaunchConfigModel
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.statistics.graphs.launchrate.LaunchRateContract
import uk.co.zac_h.spacex.statistics.graphs.launchrate.LaunchRateInteractorImpl
import uk.co.zac_h.spacex.statistics.graphs.launchrate.LaunchRatePresenterImpl
import uk.co.zac_h.spacex.utils.models.RateStatsModel

class LaunchRateTest {

    private lateinit var mPresenter: LaunchRateContract.LaunchRatePresenter
    private lateinit var presenter: LaunchRateContract.LaunchRatePresenter
    private lateinit var interactor: LaunchRateContract.LaunchRateInteractor

    @Mock
    val mInteractor: LaunchRateContract.LaunchRateInteractor =
        mock(LaunchRateContract.LaunchRateInteractor::class.java)

    @Mock
    val mView: LaunchRateContract.LaunchRateView =
        mock(LaunchRateContract.LaunchRateView::class.java)

    @Mock
    val mListener: LaunchRateContract.InteractorCallback =
        mock(LaunchRateContract.InteractorCallback::class.java)

    @Mock
    val mLaunchModelF1: LaunchesModel = mock(LaunchesModel::class.java)

    @Mock
    val mLaunchConfigModelF1: LaunchConfigModel = mock(LaunchConfigModel::class.java)

    @Mock
    val mLaunchModelF9: LaunchesModel = mock(LaunchesModel::class.java)

    @Mock
    val mLaunchConfigModelF9: LaunchConfigModel = mock(LaunchConfigModel::class.java)

    @Mock
    val mLaunchModelFailed: LaunchesModel = mock(LaunchesModel::class.java)

    @Mock
    val mLaunchModelFH: LaunchesModel = mock(LaunchesModel::class.java)

    @Mock
    val mLaunchConfigModelFH: LaunchConfigModel = mock(LaunchConfigModel::class.java)

    @Mock
    val mLaunchModelUpcoming: LaunchesModel = mock(LaunchesModel::class.java)

    private val rateStatsModel06: RateStatsModel = RateStatsModel(2006, 1f, 0f, 0f, 1f, 0f)
    private val rateStatsModel07: RateStatsModel = RateStatsModel(2007, 0f, 1f, 1f, 1f, 0f)

    private lateinit var launchesList: List<LaunchesModel>
    private lateinit var rateStatsList: List<RateStatsModel>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = LaunchRateInteractorImpl()
        mPresenter = LaunchRatePresenterImpl(mView, mInteractor)
        presenter = LaunchRatePresenterImpl(mView, interactor)

        `when`(mLaunchModelF1.rocket).thenReturn(mLaunchConfigModelF1)
        `when`(mLaunchModelF9.rocket).thenReturn(mLaunchConfigModelF9)
        `when`(mLaunchModelFH.rocket).thenReturn(mLaunchConfigModelFH)

        `when`(mLaunchModelF1.rocket.id).thenReturn("falcon1")
        `when`(mLaunchModelF1.launchDateUnix).thenReturn(1143239400) //Return date year 2006
        `when`(mLaunchModelF1.success).thenReturn(true)

        `when`(mLaunchModelF9.rocket.id).thenReturn("falcon9")
        `when`(mLaunchModelF9.launchDateUnix).thenReturn(1174439400) //Return date year 2007
        `when`(mLaunchModelF9.success).thenReturn(true)

        `when`(mLaunchModelFH.rocket.id).thenReturn("falconheavy")
        `when`(mLaunchModelFH.launchDateUnix).thenReturn(1174439400) //Return date year 2007
        `when`(mLaunchModelFH.success).thenReturn(true)

        `when`(mLaunchModelFailed.launchDateUnix).thenReturn(1143239400) //Return date year 2006
        `when`(mLaunchModelFailed.success).thenReturn(false)

        `when`(mLaunchModelUpcoming.upcoming).thenReturn(true)
        `when`(mLaunchModelUpcoming.launchDateUnix).thenReturn(1174439400) //Return date year 2007

        launchesList = listOf(
            mLaunchModelF1,
            mLaunchModelF9,
            mLaunchModelFH,
            mLaunchModelFailed,
            mLaunchModelUpcoming
        )
        rateStatsList = listOf(rateStatsModel06, rateStatsModel07)
    }

    @Test
    fun `When get launches then add to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getQueriedLaunches()
            } doReturn Calls.response(Response.success(launchesList))
        }

        presenter.getLaunchList(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            updateBarChart(rateStatsList, true)
        }
    }

    @Test
    fun `When add stats list then add to view`() {
        mPresenter.addLaunchList(rateStatsList)
        verify(mView).updateBarChart(rateStatsList, false)
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getQueriedLaunches()
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        interactor.getLaunches(api = mockRepo, listener = mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getQueriedLaunches()
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        presenter.getLaunchList(mockRepo)

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