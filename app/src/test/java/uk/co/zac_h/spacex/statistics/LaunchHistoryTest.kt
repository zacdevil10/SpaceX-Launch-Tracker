package uk.co.zac_h.spacex.statistics

import com.nhaarman.mockitokotlin2.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.mock.Calls
import uk.co.zac_h.spacex.model.spacex.LaunchConfigModel
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.statistics.graphs.launchhistory.LaunchHistoryContract
import uk.co.zac_h.spacex.statistics.graphs.launchhistory.LaunchHistoryInteractorImpl
import uk.co.zac_h.spacex.statistics.graphs.launchhistory.LaunchHistoryPresenterImpl
import uk.co.zac_h.spacex.utils.models.HistoryStatsModel

class LaunchHistoryTest {

    private lateinit var mPresenter: LaunchHistoryContract.LaunchHistoryPresenter
    private lateinit var presenter: LaunchHistoryContract.LaunchHistoryPresenter
    private lateinit var interactor: LaunchHistoryContract.LaunchHistoryInteractor

    @Mock
    val mInteractor: LaunchHistoryContract.LaunchHistoryInteractor =
        mock(LaunchHistoryContract.LaunchHistoryInteractor::class.java)

    @Mock
    val mView: LaunchHistoryContract.LaunchHistoryView =
        mock(LaunchHistoryContract.LaunchHistoryView::class.java)

    @Mock
    val mListener: LaunchHistoryContract.InteractorCallback =
        mock(LaunchHistoryContract.InteractorCallback::class.java)

    @Mock
    val mLaunchModelF1: LaunchesModel = mock(LaunchesModel::class.java)

    @Mock
    val mLaunchConfigModelF1: LaunchConfigModel = mock(LaunchConfigModel::class.java)

    @Mock
    val mLaunchModelF9: LaunchesModel = mock(LaunchesModel::class.java)

    @Mock
    val mLaunchConfigModelF9: LaunchConfigModel = mock(LaunchConfigModel::class.java)

    @Mock
    val mLaunchModelFH: LaunchesModel = mock(LaunchesModel::class.java)

    @Mock
    val mLaunchConfigModelFH: LaunchConfigModel = mock(LaunchConfigModel::class.java)

    private val historyStatsModelF1 = HistoryStatsModel("falcon1", 0, 1, 0)
    private val historyStatsModelF9 = HistoryStatsModel("falcon9", 1, 0, 100)
    private val historyStatsModelFH = HistoryStatsModel("falconheavy", 1, 0, 100)

    private lateinit var launchesList: List<LaunchesModel>
    private lateinit var historyList: List<HistoryStatsModel>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = LaunchHistoryInteractorImpl()
        mPresenter = LaunchHistoryPresenterImpl(mView, mInteractor)
        presenter = LaunchHistoryPresenterImpl(mView, interactor)

        `when`(mLaunchModelF1.rocket).thenReturn(mLaunchConfigModelF1)
        `when`(mLaunchModelF9.rocket).thenReturn(mLaunchConfigModelF9)
        `when`(mLaunchModelFH.rocket).thenReturn(mLaunchConfigModelFH)

        launchesList = listOf(mLaunchModelF1, mLaunchModelF9, mLaunchModelFH)
        historyList = listOf(historyStatsModelF1, historyStatsModelF9, historyStatsModelFH)
    }

    @Test
    fun `When get past launches then add to stats model`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getLaunches("past", "asc")
            } doReturn Calls.response(Response.success(launchesList))
        }

        `when`(mLaunchModelF1.rocket.id).thenReturn("falcon1")
        `when`(mLaunchModelF1.success).thenReturn(false)
        `when`(mLaunchModelF9.rocket.id).thenReturn("falcon9")
        `when`(mLaunchModelF9.success).thenReturn(true)
        `when`(mLaunchModelFH.rocket.id).thenReturn("falconheavy")
        `when`(mLaunchModelFH.success).thenReturn(true)

        presenter.getLaunchList(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            updatePieChart(historyList, false)
            setSuccessRate(historyList, false)
        }
    }

    @Test
    fun `When toggle filter then show in view`() {
        mPresenter.showFilter(true)

        verify(mView).showFilter(true)
    }

    @Test
    fun `When add stats to view then update chart`() {
        mPresenter.addLaunchList(historyList)

        verify(mView).updatePieChart(historyList, false)
        verify(mView).setSuccessRate(historyList, false)
    }

    @Test
    fun `When updating success filter then set filter type in view`() {
        mPresenter.updateFilter(historyList, "success", true)

        verify(mView).setFilterSuccessful(true)
        verify(mView).updatePieChart(historyList, false)
    }

    @Test
    fun `When updating failure filter then set filter type in view`() {
        mPresenter.updateFilter(historyList, "failed", true)

        verify(mView).setFilterFailed(true)
        verify(mView).updatePieChart(historyList, false)
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getLaunches("past", "asc")
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
                getLaunches("past", "asc")
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
    fun `When HttpException occurs`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getLaunches("past", "asc")
            } doThrow HttpException(
                Response.error<Any>(
                    500,
                    "Test server error".toResponseBody("text/plain".toMediaTypeOrNull())
                )
            )
        }

        interactor.getLaunches(api = mockRepo, listener = mListener)

        verifyBlocking(mListener) { onError("HTTP 500 Response.error()") }
    }

    @Test(expected = Throwable::class)
    fun `When job fails to execute`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getLaunches("past", "asc")
            } doThrow Throwable()
        }

        interactor.getLaunches(api = mockRepo, listener = mListener)
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequests()

        Mockito.verify(mInteractor).cancelAllRequests()
    }

}