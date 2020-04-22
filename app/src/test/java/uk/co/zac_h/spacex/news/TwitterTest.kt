package uk.co.zac_h.spacex.news

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
import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.news.twitter.TwitterFeedContract
import uk.co.zac_h.spacex.news.twitter.TwitterFeedInteractorImpl
import uk.co.zac_h.spacex.news.twitter.TwitterFeedPresenterImpl
import uk.co.zac_h.spacex.rest.TwitterInterface

class TwitterTest {

    private lateinit var mPresenter: TwitterFeedContract.TwitterFeedPresenter
    private lateinit var presenter: TwitterFeedContract.TwitterFeedPresenter
    private lateinit var interactor: TwitterFeedContract.TwitterFeedInteractor
    @Mock
    val mInteractor: TwitterFeedContract.TwitterFeedInteractor =
        mock(TwitterFeedContract.TwitterFeedInteractor::class.java)
    @Mock
    val mView: TwitterFeedContract.TwitterFeedView =
        mock(TwitterFeedContract.TwitterFeedView::class.java)
    @Mock
    val mListener: TwitterFeedContract.InteractorCallback =
        mock(TwitterFeedContract.InteractorCallback::class.java)
    @Mock
    val mTwitterModel: TimelineTweetModel = mock(TimelineTweetModel::class.java)

    private lateinit var twitterList: List<TimelineTweetModel>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = TwitterFeedInteractorImpl()
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
            } doReturn Calls.response(Response.success(twitterList))
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
            } doReturn Calls.response(Response.success(twitterList))
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
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
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
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
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