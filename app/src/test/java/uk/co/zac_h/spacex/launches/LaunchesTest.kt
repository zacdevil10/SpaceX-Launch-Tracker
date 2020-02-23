package uk.co.zac_h.spacex.launches

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
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

class LaunchesTest {

    private lateinit var mPresenter: LaunchesPresenter
    private lateinit var presenter: LaunchesPresenter
    private lateinit var interactor: LaunchesInteractor
    @Mock
    val mInteractor: LaunchesInteractor = mock(LaunchesInteractor::class.java)
    @Mock
    val mView: LaunchesView = mock(LaunchesView::class.java)
    @Mock
    val mListener: LaunchesInteractor.InteractorCallback =
        mock(LaunchesInteractor.InteractorCallback::class.java)
    @Mock
    val mLaunchesModel: LaunchesModel = mock(LaunchesModel::class.java)

    private lateinit var launchesList: List<LaunchesModel>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = LaunchesInteractorImpl(Dispatchers.Unconfined)
        mPresenter = LaunchesPresenterImpl(mView, mInteractor)
        presenter = LaunchesPresenterImpl(mView, interactor)

        launchesList = listOf(mLaunchesModel)
    }

    @Test
    fun `When get future launches then add ascending to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getLaunches("upcoming", "asc") } doReturn Response.success(launchesList)
        }

        presenter.getLaunchList("upcoming", mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            updateLaunchesList(launchesList)
        }
    }

    @Test
    fun `When get past launches then add descending to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getLaunches("past", "desc") } doReturn Response.success(launchesList)
        }

        presenter.getLaunchList("past", mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            updateLaunchesList(launchesList)
        }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getLaunches("past", "desc") } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        interactor.getLaunches("past", "desc", mockRepo, mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getLaunches("past", "desc") } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        presenter.getLaunchList("past", mockRepo)

        verifyBlocking(mView) {
            showProgress()
            showError("Error: 404")
            toggleSwipeProgress(false)
        }
    }

    @Test
    fun `When HttpException occurs`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getLaunches("past", "desc") } doThrow HttpException(
                Response.error<Any>(
                    500,
                    "Test server error".toResponseBody("text/plain".toMediaTypeOrNull())
                )
            )
        }

        interactor.getLaunches("past", "desc", mockRepo, mListener)

        verifyBlocking(mListener) { onError("HTTP 500 Response.error()") }
    }

    @Test(expected = Throwable::class)
    fun `When job fails to execute`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getLaunches("past", "desc") } doThrow Throwable()
        }

        interactor.getLaunches("past", "desc", mockRepo, mListener)
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequests()

        Mockito.verify(mInteractor).cancelAllRequests()
    }

}