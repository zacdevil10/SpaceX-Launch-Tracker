package uk.co.zac_h.spacex.news

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
import uk.co.zac_h.spacex.model.reddit.SubredditModel
import uk.co.zac_h.spacex.news.reddit.RedditFeedContract
import uk.co.zac_h.spacex.news.reddit.RedditFeedInteractorImpl
import uk.co.zac_h.spacex.news.reddit.RedditFeedPresenterImpl
import uk.co.zac_h.spacex.rest.RedditInterface

class RedditTest {

    private lateinit var mPresenter: RedditFeedContract.RedditFeedPresenter
    private lateinit var presenter: RedditFeedContract.RedditFeedPresenter
    private lateinit var interactor: RedditFeedContract.RedditFeedInteractor

    @Mock
    val mInteractor: RedditFeedContract.RedditFeedInteractor =
        mock(RedditFeedContract.RedditFeedInteractor::class.java)

    @Mock
    val mView: RedditFeedContract.RedditFeedView =
        mock(RedditFeedContract.RedditFeedView::class.java)

    @Mock
    val mListener: RedditFeedContract.InteractorCallback =
        mock(RedditFeedContract.InteractorCallback::class.java)

    @Mock
    val mRedditModel: SubredditModel = mock(SubredditModel::class.java)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = RedditFeedInteractorImpl()
        mPresenter = RedditFeedPresenterImpl(mView, mInteractor)
        presenter = RedditFeedPresenterImpl(mView, interactor)
    }

    @Test
    fun `When get reddit feed then add to view`() {
        val mockRepo = mock<RedditInterface> {
            onBlocking { getRedditFeed("SpaceX", "asc") } doReturn Calls.response(
                Response.success(
                    mRedditModel
                )
            )
        }

        presenter.getSub("asc", mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            updateRecycler(mRedditModel)
        }
    }

    @Test
    fun `When getting next page then add to existing adapter`() {
        val mockRepo = mock<RedditInterface> {
            onBlocking {
                getRedditFeed("SpaceX", "asc", "id")
            } doReturn Calls.response(Response.success(mRedditModel))
        }

        presenter.getNextPage("id", "asc", mockRepo)

        verifyBlocking(mView) {
            showPagingProgress()
            hidePagingProgress()
            addPagedData(mRedditModel)
        }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<RedditInterface> {
            onBlocking { getRedditFeed("SpaceX", "asc") } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        interactor.getSubreddit(mockRepo, mListener, "asc")

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequest()

        Mockito.verify(mInteractor).cancelAllRequests()
    }
}