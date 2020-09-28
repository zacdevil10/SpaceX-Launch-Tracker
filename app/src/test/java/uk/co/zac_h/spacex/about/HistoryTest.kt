package uk.co.zac_h.spacex.about

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.Response
import retrofit2.mock.Calls
import uk.co.zac_h.spacex.about.history.HistoryContract
import uk.co.zac_h.spacex.about.history.HistoryInteractorImpl
import uk.co.zac_h.spacex.about.history.HistoryPresenterImpl
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel
import uk.co.zac_h.spacex.utils.splitHistoryListByDate

class HistoryTest {

    private lateinit var mPresenter: HistoryContract.HistoryPresenter
    private lateinit var presenter: HistoryContract.HistoryPresenter
    private lateinit var interactor: HistoryContract.HistoryInteractor

    @Mock
    val mInteractor: HistoryContract.HistoryInteractor =
        mock(HistoryContract.HistoryInteractor::class.java)

    @Mock
    val mView: HistoryContract.HistoryView = mock(HistoryContract.HistoryView::class.java)

    @Mock
    val mListener: HistoryContract.InteractorCallback =
        mock(HistoryContract.InteractorCallback::class.java)

    lateinit var query: QueryModel

    private val historyModel = HistoryModel(
        HistoryLinksModel(null),
        "History 1",
        "2008-09-28T23:15:00Z",
        1222643700,
        "Details",
        "id",

        )
    private lateinit var historyArray: List<HistoryModel>
    private lateinit var historyDocsModel: HistoryDocsModel
    private val historyHeaderArray = ArrayList<HistoryHeaderModel>()

    private lateinit var mockRepoSuccess: SpaceXInterface
    private lateinit var mockRepoError: SpaceXInterface

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = HistoryInteractorImpl()
        mPresenter = HistoryPresenterImpl(mView, mInteractor)
        presenter = HistoryPresenterImpl(mView, interactor)

        query = QueryModel(
            "",
            QueryOptionsModel(
                false,
                "",
                QueryHistorySort("asc"),
                "",
                1000000
            )
        )

        historyArray = listOf(historyModel)
        historyHeaderArray.splitHistoryListByDate(historyArray)
        historyDocsModel = HistoryDocsModel(historyArray)

        mockRepoSuccess = mock {
            onBlocking { getHistory(query) } doReturn Calls.response(
                Response.success(
                    historyDocsModel
                )
            )
        }

        mockRepoError = mock {
            onBlocking { getHistory(query) } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }
    }

    @Test
    fun `When getHistory is called verify show progress`() {
        presenter.getHistory(false, mockRepoSuccess)

        verifyBlocking(mView) { showProgress() }
        verifyBlocking(mView) { hideProgress() }
        verifyBlocking(mView) { addHistory(historyHeaderArray) }
        verifyBlocking(mView) { toggleSwipeProgress(false) }
    }

    @Test
    fun `Get data from API and return to presenter`() {
        interactor.getAllHistoricEvents("asc", mockRepoSuccess, mListener)

        verifyBlocking(mListener) { onSuccess(historyArray) }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        interactor.getAllHistoricEvents("asc", mockRepoError, mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `When response from API fails then show error`() {
        presenter.getHistory(false, mockRepoError)

        verifyBlocking(mView) {
            showProgress()
            showError("Error: 404")
            toggleSwipeProgress(false)
        }
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequest()

        verify(mInteractor).cancelAllRequests()
    }

}