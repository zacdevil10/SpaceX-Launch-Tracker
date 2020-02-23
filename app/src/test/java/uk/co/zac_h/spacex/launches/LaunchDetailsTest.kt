package uk.co.zac_h.spacex.launches

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import uk.co.zac_h.spacex.launches.details.*
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper

@ExperimentalCoroutinesApi
class LaunchDetailsTest {

    private lateinit var mPresenter: LaunchDetailsPresenter
    private lateinit var presenter: LaunchDetailsPresenter
    private lateinit var interactor: LaunchDetailsInteractor
    @Mock
    val mInteractor: LaunchDetailsInteractor = mock(LaunchDetailsInteractor::class.java)
    @Mock
    val mView: LaunchDetailsView = mock(LaunchDetailsView::class.java)
    @Mock
    val mListener: LaunchDetailsInteractor.InteractorCallback =
        mock(LaunchDetailsInteractor.InteractorCallback::class.java)
    @Mock
    val mPinnedSharedPreferencesHelper: PinnedSharedPreferencesHelper =
        mock(PinnedSharedPreferencesHelper::class.java)
    @Mock
    val mLaunchesModel: LaunchesModel = mock(LaunchesModel::class.java)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = LaunchDetailsInteractorImpl(Dispatchers.Unconfined)
        mPresenter = LaunchDetailsPresenterImpl(mView, mPinnedSharedPreferencesHelper, mInteractor)
        presenter = LaunchDetailsPresenterImpl(mView, mPinnedSharedPreferencesHelper, interactor)
    }

    @Test
    fun `When launch id is provided then get launch data`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getSingleLaunch("1") } doReturn Response.success(mLaunchesModel)
        }

        interactor.getSingleLaunch("1", mockRepo, mListener)

        verifyBlocking(mListener) { onSuccess(mLaunchesModel) }
    }

    @Test
    fun `When LaunchModel is provided then add to view`() {
        mPresenter.addLaunchModel(mLaunchesModel)

        verify(mView).updateLaunchDataView(mLaunchesModel)
    }

    @Test
    fun `When get launch data then add to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getSingleLaunch("1") } doReturn Response.success(mLaunchesModel)
        }

        presenter.getLaunch("1", mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            updateLaunchDataView(mLaunchesModel)
        }
    }

    @Test
    fun `When creating event then call newCalendarEvent`() {
        mPresenter.createEvent()

        verify(mView).newCalendarEvent()
    }

    @Test
    fun `When pinning a launch then add to shared preferences`() {
        mPresenter.pinLaunch("1", true)

        verify(mPinnedSharedPreferencesHelper).addPinnedLaunch("1")
    }

    @Test
    fun `When unpinning a launch then remove from shared preferences`() {
        mPresenter.pinLaunch("1", false)

        verify(mPinnedSharedPreferencesHelper).removePinnedLaunch("1")
    }

    @Test
    fun `When checking if launch is pinned then return pinned status`() {
        `when`(mPinnedSharedPreferencesHelper.isPinned("1")).thenReturn(true)

        assert(mPresenter.isPinned("1"))
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getSingleLaunch("1") } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        interactor.getSingleLaunch("1", mockRepo, mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getSingleLaunch("1") } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        presenter.getLaunch("1", mockRepo)

        verifyBlocking(mView) {
            showProgress()
            showError("Error: 404")
        }
    }

    @Test
    fun `When HttpException occurs`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getSingleLaunch("1") } doThrow HttpException(
                Response.error<Any>(
                    500,
                    "Test server error".toResponseBody("text/plain".toMediaTypeOrNull())
                )
            )
        }

        interactor.getSingleLaunch("1", mockRepo, mListener)

        verifyBlocking(mListener) { onError("HTTP 500 Response.error()") }
    }

    @Test(expected = Throwable::class)
    fun `When job fails to execute`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getSingleLaunch("1") } doThrow Throwable()
        }

        interactor.getSingleLaunch("1", mockRepo, mListener)
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequest()

        Mockito.verify(mInteractor).cancelRequest()
    }
}