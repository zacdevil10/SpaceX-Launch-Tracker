package uk.co.zac_h.spacex.crew

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
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface

class CrewTest {

    private lateinit var mPresenter: CrewContract.CrewPresenter
    private lateinit var presenter: CrewContract.CrewPresenter
    private lateinit var interactor: CrewContract.CrewInteractor

    @Mock
    val mInteractor: CrewContract.CrewInteractor =
        Mockito.mock(CrewContract.CrewInteractor::class.java)

    @Mock
    val mView: CrewContract.CrewView =
        Mockito.mock(CrewContract.CrewView::class.java)

    @Mock
    val mListener: CrewContract.InteractorCallback =
        Mockito.mock(CrewContract.InteractorCallback::class.java)

    @Mock
    val mCrewModel: CrewModel =
        Mockito.mock(CrewModel::class.java)

    private val mCrewDocsModel: CrewDocsModel =
        CrewDocsModel(listOf(mCrewModel))

    private lateinit var query: QueryModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = CrewInteractorImpl()
        mPresenter = CrewPresenterImpl(mView, mInteractor)
        presenter = CrewPresenterImpl(mView, interactor)

        query = QueryModel(
            "",
            QueryOptionsModel(
                false,
                listOf(
                    QueryPopulateModel(
                        "launches",
                        select = listOf("flight_number", "name", "date_unix"),
                        populate = ""
                    )
                ), "", "", 100000
            )
        )
    }

    @Test
    fun `When launch id is provided then get launch cores data`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getCrew(query) } doReturn Calls.response(
                Response.success(
                    mCrewDocsModel
                )
            )
        }

        interactor.getCrew(mockRepo, mListener)

        verifyBlocking(mListener) {
            onSuccess(mCrewDocsModel)
        }
    }

    @Test
    fun `Add launch data to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getCrew(query) } doReturn Calls.response(
                Response.success(
                    mCrewDocsModel
                )
            )
        }

        presenter.getCrew(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            updateCrew(mCrewDocsModel.docs)
        }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getCrew(query) } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        interactor.getCrew(mockRepo, mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getCrew(query) } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        presenter.getCrew(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            showError("Error: 404")
        }
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequest()

        Mockito.verify(mInteractor).cancelAllRequests()
    }
}