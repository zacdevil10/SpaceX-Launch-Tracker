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
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
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
    val mLaunchesModel: LaunchesModel = mock(LaunchesModel::class.java)

    private lateinit var launchesList: List<LaunchesModel>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = LaunchesInteractorImpl()
        mPresenter = LaunchesPresenterImpl(mView, mInteractor)
        presenter = LaunchesPresenterImpl(mView, interactor)

        launchesList = listOf(mLaunchesModel)
    }

    @Test
    fun `When get future launches then add ascending to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getLaunches("upcoming", "asc") } doReturn Calls.response(
                Response.success(
                    launchesList
                )
            )
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
            onBlocking { getLaunches("past", "desc") } doReturn Calls.response(
                Response.success(
                    launchesList
                )
            )
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
            onBlocking { getLaunches("past", "desc") } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        interactor.getLaunches("past", "desc", mockRepo, mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getLaunches("past", "desc") } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
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
    fun `Cancel request`() {
        mPresenter.cancelRequests()

        Mockito.verify(mInteractor).cancelAllRequests()
    }

}