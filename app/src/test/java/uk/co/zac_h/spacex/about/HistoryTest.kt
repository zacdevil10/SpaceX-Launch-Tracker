package uk.co.zac_h.spacex.about

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyBlocking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import uk.co.zac_h.spacex.about.history.*
import uk.co.zac_h.spacex.model.spacex.HistoryLinksModel
import uk.co.zac_h.spacex.model.spacex.HistoryModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel
import uk.co.zac_h.spacex.utils.splitHistoryListByDate

@ExperimentalCoroutinesApi
class HistoryTest {

    private lateinit var mPresenter: HistoryPresenter
    private lateinit var presenter: HistoryPresenter
    private lateinit var interactor: HistoryInteractor
    @Mock
    val mInteractor: HistoryInteractor = mock(HistoryInteractor::class.java)
    @Mock
    val mView: HistoryView = mock(HistoryView::class.java)
    @Mock
    val mListener: HistoryInteractor.Callback = mock(HistoryInteractor.Callback::class.java)

    private val historyModel = HistoryModel(
        1,
        "History 1",
        "2008-09-28T23:15:00Z",
        1222643700,
        4,
        "Details",
        HistoryLinksModel(null, null, null)
    )
    private lateinit var historyArray: List<HistoryModel>
    private val historyHeaderArray = ArrayList<HistoryHeaderModel>()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = HistoryInteractorImpl(Dispatchers.Unconfined)
        mPresenter = HistoryPresenterImpl(mView, mInteractor)
        presenter = HistoryPresenterImpl(mView, interactor)

        historyArray = listOf(historyModel)

        historyHeaderArray.splitHistoryListByDate(historyArray)
    }

    @Test
    fun `When getHistory is called verify show progress`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getHistory("desc") } doReturn Response.success(historyArray)
        }

        presenter.getHistory(mockRepo)

        verifyBlocking(mView) { showProgress() }
        verifyBlocking(mView) { hideProgress() }
        verifyBlocking(mView) { addHistory(historyHeaderArray) }
        verifyBlocking(mView) { toggleSwipeProgress(false) }
    }

    @Test
    fun `Get data from API and return to presenter`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getHistory("desc") } doReturn Response.success(historyArray)
        }

        interactor.getAllHistoricEvents(mockRepo, mListener)

        verifyBlocking(mListener) { onSuccess(historyArray) }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getHistory("desc") } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        interactor.getAllHistoricEvents(mockRepo, mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `When response from API fails then show error`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getHistory("desc") } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        presenter.getHistory(mockRepo)

        verifyBlocking(mView) { showProgress() }
        verifyBlocking(mView) { showError("Error: 404") }
        verifyBlocking(mView) { toggleSwipeProgress(false) }
    }

    @Test
    fun `When HttpException occurs`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getHistory("desc") } doThrow HttpException(
                Response.error<Any>(
                    500,
                    "Test server error".toResponseBody("text/plain".toMediaTypeOrNull())
                )
            )
        }

        interactor.getAllHistoricEvents(mockRepo, mListener)

        verifyBlocking(mListener) { onError("HTTP 500 Response.error()") }
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequest()

        verify(mInteractor).cancelAllRequests()
    }

}