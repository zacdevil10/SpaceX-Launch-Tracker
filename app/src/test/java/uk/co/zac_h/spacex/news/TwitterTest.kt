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
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.news.twitter.*
import uk.co.zac_h.spacex.rest.TwitterInterface

class TwitterTest {

    private lateinit var mPresenter: TwitterFeedPresenter
    private lateinit var presenter: TwitterFeedPresenter
    private lateinit var interactor: TwitterFeedInteractor
    @Mock
    val mInteractor: TwitterFeedInteractor = mock(TwitterFeedInteractor::class.java)
    @Mock
    val mView: TwitterFeedView = mock(TwitterFeedView::class.java)
    @Mock
    val mListener: TwitterFeedInteractor.Callback = mock(TwitterFeedInteractor.Callback::class.java)
    @Mock
    val mTwitterModel: TimelineTweetModel = mock(TimelineTweetModel::class.java)

    private lateinit var twitterList: List<TimelineTweetModel>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = TwitterFeedInteractorImpl(Dispatchers.Unconfined)
        mPresenter = TwitterFeedPresenterImpl(mView, mInteractor)
        presenter = TwitterFeedPresenterImpl(mView, interactor)

        twitterList = listOf(mTwitterModel)
    }

    @Test
    fun `When get twitter feed then add to view`() {
        val mockRepo = mock<TwitterInterface> {
            onBlocking {
                getTweets(
                    "SpaceX",
                    rts = false,
                    trim = false,
                    mode = "extended",
                    count = 15
                )
            } doReturn Response.success(twitterList)
        }

        presenter.getTweets(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            updateRecycler(twitterList)
        }
    }

    @Test
    fun `When getting next page then add to existing adapter`() {
        val mockRepo = mock<TwitterInterface> {
            onBlocking {
                getTweets(
                    "SpaceX",
                    rts = false,
                    trim = false,
                    mode = "extended",
                    count = 15,
                    maxId = 1L
                )
            } doReturn Response.success(twitterList)
        }

        presenter.getTweets(1L, mockRepo)

        verifyBlocking(mView) {
            showPagingProgress()
            hidePagingProgress()
            addPagedData(twitterList)
        }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<TwitterInterface> {
            onBlocking {
                getTweets(
                    "SpaceX",
                    rts = false,
                    trim = false,
                    mode = "extended",
                    count = 15
                )
            } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        interactor.getTwitterTimeline(api = mockRepo, listener = mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<TwitterInterface> {
            onBlocking {
                getTweets(
                    "SpaceX",
                    rts = false,
                    trim = false,
                    mode = "extended",
                    count = 15
                )
            } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        presenter.getTweets(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            showError("Error: 404")
            toggleSwipeProgress(false)
        }
    }

    @Test
    fun `When HttpException occurs`() {
        val mockRepo = mock<TwitterInterface> {
            onBlocking {
                getTweets(
                    "SpaceX",
                    rts = false,
                    trim = false,
                    mode = "extended",
                    count = 15
                )
            } doThrow HttpException(
                Response.error<Any>(
                    500,
                    "Test server error".toResponseBody("text/plain".toMediaTypeOrNull())
                )
            )
        }

        interactor.getTwitterTimeline(api = mockRepo, listener = mListener)

        verifyBlocking(mListener) { onError("HTTP 500 Response.error()") }
    }

    @Test(expected = Throwable::class)
    fun `When job fails to execute`() {
        val mockRepo = mock<TwitterInterface> {
            onBlocking {
                getTweets(
                    "SpaceX",
                    rts = false,
                    trim = false,
                    mode = "extended",
                    count = 15
                )
            } doThrow Throwable()
        }

        interactor.getTwitterTimeline(api = mockRepo, listener = mListener)
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequests()

        verify(mInteractor).cancelAllRequests()
    }

    @Test
    fun `Show scroll up button`() {
        mPresenter.toggleScrollUp(true)

        verify(mView).showScrollUp()
    }

    @Test
    fun `Hide scroll up button`() {
        mPresenter.toggleScrollUp(false)

        verify(mView).hideScrollUp()
    }
}