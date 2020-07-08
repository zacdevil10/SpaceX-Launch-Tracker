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
import uk.co.zac_h.spacex.launches.details.details.LaunchDetailsContract
import uk.co.zac_h.spacex.launches.details.details.LaunchDetailsInteractorImpl
import uk.co.zac_h.spacex.launches.details.details.LaunchDetailsPresenterImpl
import uk.co.zac_h.spacex.model.spacex.*
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

    private lateinit var mockRepoSuccess: SpaceXInterface
    private lateinit var mockRepoError: SpaceXInterface

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor =
            LaunchDetailsInteractorImpl()
        mPresenter =
            LaunchDetailsPresenterImpl(
                mView,
                mPinnedSharedPreferencesHelper,
                mInteractor
            )
        presenter =
            LaunchDetailsPresenterImpl(
                mView,
                mPinnedSharedPreferencesHelper,
                interactor
            )

        val populateList = listOf(
            QueryPopulateModel("launchpad", select = listOf("name"), populate = ""),
            QueryPopulateModel("rocket", populate = "", select = listOf("name"))
        )

        query = QueryModel(
            QueryLaunchesQueryModel("1"),
            QueryOptionsModel(
                true,
                populate = populateList,
                sort = "",
                select = listOf(
                    "links",
                    "static_fire_date_unix",
                    "tbd",
                    "net",
                    "rocket",
                    "details",
                    "launchpad",
                    "flight_number",
                    "name",
                    "date_unix"
                ),
                limit = 1
            )
        )

        mockRepoSuccess = mock {
            onBlocking { getQueriedLaunches(query) } doReturn Calls.response(
                Response.success(
                    mLaunchesExtendedDocsModel
                )
            )
        }

        mockRepoError = mock {
            onBlocking { getQueriedLaunches(query) } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }
    }

    @Test
    fun `When launch id is provided then get launch data`() {
        interactor.getSingleLaunch("1", mockRepoSuccess, mListener)

        verifyBlocking(mListener) { onSuccess(mLaunchesExtendedDocsModel) }
    }

    @Test
    fun `When LaunchModel is provided then add to view`() {
        mPresenter.addLaunchModel(mLaunchesExtendedModel, true)

        verify(mView).updateLaunchDataView(mLaunchesExtendedModel, true)
    }

    @Test
    fun `When get launch data then add to view`() {
        presenter.getLaunch("1", mockRepoSuccess)

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
        interactor.getSingleLaunch("1", mockRepoError, mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        presenter.getLaunch("1", mockRepoError)

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