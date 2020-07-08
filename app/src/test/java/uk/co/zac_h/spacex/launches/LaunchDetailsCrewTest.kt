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
import org.mockito.MockitoAnnotations
import retrofit2.Response
import retrofit2.mock.Calls
import uk.co.zac_h.spacex.crew.CrewContract
import uk.co.zac_h.spacex.launches.details.crew.LaunchDetailsCrewContract
import uk.co.zac_h.spacex.launches.details.crew.LaunchDetailsCrewInteractor
import uk.co.zac_h.spacex.launches.details.crew.LaunchDetailsCrewPresenter
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface

class LaunchDetailsCrewTest {

    private lateinit var mPresenter: LaunchDetailsCrewContract.Presenter
    private lateinit var presenter: LaunchDetailsCrewContract.Presenter
    private lateinit var interactor: LaunchDetailsCrewContract.Interactor

    @Mock
    val mInteractor: LaunchDetailsCrewContract.Interactor =
        Mockito.mock(LaunchDetailsCrewContract.Interactor::class.java)

    @Mock
    val mView: CrewContract.CrewView =
        Mockito.mock(CrewContract.CrewView::class.java)

    @Mock
    val mListener: LaunchDetailsCrewContract.InteractorCallback =
        Mockito.mock(LaunchDetailsCrewContract.InteractorCallback::class.java)

    @Mock
    val mLaunchesExtendedModel: LaunchesExtendedModel =
        Mockito.mock(LaunchesExtendedModel::class.java)

    private val mLaunchesExtendedDocsModel: LaunchesExtendedDocsModel =
        LaunchesExtendedDocsModel(listOf(mLaunchesExtendedModel))

    private lateinit var query: QueryModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = LaunchDetailsCrewInteractor()
        mPresenter = LaunchDetailsCrewPresenter(mView, mInteractor)
        presenter = LaunchDetailsCrewPresenter(mView, interactor)

        val populateList = listOf(
            QueryPopulateModel(
                "crew",
                populate = listOf(
                    QueryPopulateModel(
                        "launches",
                        populate = "",
                        select = listOf("flight_number", "name", "date_unix")
                    )
                ),
                select = ""
            )
        )

        query = QueryModel(
            QueryLaunchesQueryModel("id"),
            QueryOptionsModel(false, populateList, "", listOf("crew"), 1)
        )
    }

    @Test
    fun `When launch id is provided then get launch cores data`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getQueriedLaunches(query) } doReturn Calls.response(
                Response.success(
                    mLaunchesExtendedDocsModel
                )
            )
        }

        interactor.getCrew("id", mockRepo, mListener)

        verifyBlocking(mListener) {
            onSuccess(mLaunchesExtendedDocsModel)
        }
    }

    @Test
    fun `Add launch data to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getQueriedLaunches(query) } doReturn Calls.response(
                Response.success(
                    mLaunchesExtendedDocsModel
                )
            )
        }

        presenter.getCrew("id", mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            mLaunchesExtendedDocsModel.docs[0].crew?.let {
                updateCrew(it)
            }
        }
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

        interactor.getCrew("id", mockRepo, mListener)

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

        presenter.getCrew("id", mockRepo)

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