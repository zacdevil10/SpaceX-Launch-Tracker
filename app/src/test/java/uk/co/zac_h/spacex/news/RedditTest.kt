package uk.co.zac_h.spacex.news

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
import uk.co.zac_h.spacex.model.reddit.SubredditModel
import uk.co.zac_h.spacex.news.reddit.*
import uk.co.zac_h.spacex.rest.RedditInterface

class RedditTest {

    private lateinit var mPresenter: RedditFeedPresenter
    private lateinit var presenter: RedditFeedPresenter
    private lateinit var interactor: RedditFeedInteractor
    @Mock
    val mInteractor: RedditFeedInteractor = mock(RedditFeedInteractor::class.java)
    @Mock
    val mView: RedditFeedView = mock(RedditFeedView::class.java)
    @Mock
    val mListener: RedditFeedInteractor.Callback = mock(RedditFeedInteractor.Callback::class.java)
    @Mock
    val mRedditModel: SubredditModel = mock(SubredditModel::class.java)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = RedditFeedInteractorImpl(Dispatchers.Unconfined)
        mPresenter = RedditFeedPresenterImpl(mView, mInteractor)
        presenter = RedditFeedPresenterImpl(mView, interactor)
    }

    @Test
    fun `When get reddit feed then add to view`() {
        val mockRepo = mock<RedditInterface> {
            onBlocking { getRedditFeed("SpaceX", "asc") } doReturn Response.success(mRedditModel)
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
            onBlocking { getRedditFeed("SpaceX", "asc", "id") } doReturn Response.success(
                mRedditModel
            )
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
            onBlocking { getRedditFeed("SpaceX", "asc") } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        interactor.getSubreddit(mockRepo, mListener, "asc")

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<RedditInterface> {
            onBlocking { getRedditFeed("SpaceX", "asc") } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        presenter.getSub("asc", mockRepo)

        verifyBlocking(mView) {
            showProgress()
            showError("Error: 404")
            toggleSwipeRefresh(false)
        }
    }

    @Test
    fun `When HttpException occurs`() {
        val mockRepo = mock<RedditInterface> {
            onBlocking { getRedditFeed("SpaceX", "asc") } doThrow HttpException(
                Response.error<Any>(
                    500,
                    "Test server error".toResponseBody("text/plain".toMediaTypeOrNull())
                )
            )
        }

        interactor.getSubreddit(mockRepo, mListener, "asc")

        verifyBlocking(mListener) { onError("HTTP 500 Response.error()") }
    }

    @Test(expected = Throwable::class)
    fun `When job fails to execute`() {
        val mockRepo = mock<RedditInterface> {
            onBlocking { getRedditFeed("SpaceX") } doThrow Throwable()
        }

        interactor.getSubreddit(mockRepo, mListener, "asc")
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequest()

        Mockito.verify(mInteractor).cancelAllRequests()
    }
}