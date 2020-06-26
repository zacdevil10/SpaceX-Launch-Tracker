package uk.co.zac_h.spacex.launches

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
import uk.co.zac_h.spacex.launches.details.LaunchDetailsContract
import uk.co.zac_h.spacex.launches.details.LaunchDetailsInteractorImpl
import uk.co.zac_h.spacex.launches.details.LaunchDetailsPresenterImpl
import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedDocsModel
import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedModel
import uk.co.zac_h.spacex.model.spacex.QueryLaunchesQueryModel
import uk.co.zac_h.spacex.model.spacex.QueryModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper

class LaunchDetailsTest {

    private lateinit var mPresenter: LaunchDetailsContract.LaunchDetailsPresenter
    private lateinit var presenter: LaunchDetailsContract.LaunchDetailsPresenter
    private lateinit var interactor: LaunchDetailsContract.LaunchDetailsInteractor

    @Mock
    val mInteractor: LaunchDetailsContract.LaunchDetailsInteractor =
        mock(LaunchDetailsContract.LaunchDetailsInteractor::class.java)

    @Mock
    val mView: LaunchDetailsContract.LaunchDetailsView =
        mock(LaunchDetailsContract.LaunchDetailsView::class.java)

    @Mock
    val mListener: LaunchDetailsContract.InteractorCallback =
        mock(LaunchDetailsContract.InteractorCallback::class.java)

    @Mock
    val mPinnedSharedPreferencesHelper: PinnedSharedPreferencesHelper =
        mock(PinnedSharedPreferencesHelper::class.java)

    @Mock
    val mLaunchesExtendedModel: LaunchesExtendedModel = mock(LaunchesExtendedModel::class.java)

    private val mLaunchesExtendedDocsModel: LaunchesExtendedDocsModel =
        LaunchesExtendedDocsModel(listOf(mLaunchesExtendedModel))

    private lateinit var query: QueryModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = LaunchDetailsInteractorImpl()
        mPresenter = LaunchDetailsPresenterImpl(mView, mPinnedSharedPreferencesHelper, mInteractor)
        presenter = LaunchDetailsPresenterImpl(mView, mPinnedSharedPreferencesHelper, interactor)

        query = QueryModel(
            QueryLaunchesQueryModel(1),
            ""
        )
    }

    @Test
    fun `When launch id is provided then get launch data`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getQueriedLaunches(query) } doReturn Calls.response(
                Response.success(
                    mLaunchesExtendedDocsModel
                )
            )
        }

        interactor.getSingleLaunch(1, mockRepo, mListener)

        verifyBlocking(mListener) { onSuccess(mLaunchesExtendedDocsModel) }
    }

    @Test
    fun `When LaunchModel is provided then add to view`() {
        mPresenter.addLaunchModel(mLaunchesExtendedModel, true)

        verify(mView).updateLaunchDataView(mLaunchesExtendedModel, true)
    }

    @Test
    fun `When get launch data then add to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getQueriedLaunches(query) } doReturn Calls.response(
                Response.success(
                    mLaunchesExtendedDocsModel
                )
            )
        }

        presenter.getLaunch(1, mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            updateLaunchDataView(mLaunchesExtendedDocsModel.docs[0], false)
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

        verify(mPinnedSharedPreferencesHelper).setPinnedLaunch("1", true)
    }

    @Test
    fun `When unpinning a launch then remove from shared preferences`() {
        mPresenter.pinLaunch("1", false)

        verify(mPinnedSharedPreferencesHelper).setPinnedLaunch("1", false)
    }

    @Test
    fun `When checking if launch is pinned then return pinned status`() {
        `when`(mPinnedSharedPreferencesHelper.isPinned("1")).thenReturn(true)

        assert(mPresenter.isPinned("1"))
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getQueriedLaunches(query) } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        interactor.getSingleLaunch(1, mockRepo, mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getQueriedLaunches(query) } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        presenter.getLaunch(1, mockRepo)

        verifyBlocking(mView) {
            showProgress()
            showError("Error: 404")
        }
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequest()

        Mockito.verify(mInteractor).cancelRequest()
    }
}