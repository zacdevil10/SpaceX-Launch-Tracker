package uk.co.zac_h.spacex.vehicles

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
import uk.co.zac_h.spacex.model.spacex.DragonModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.dragon.*

class DragonTest {

    private lateinit var mPresenter: DragonPresenter
    private lateinit var presenter: DragonPresenter
    private lateinit var interactor: DragonInteractor

    @Mock
    val mInteractor: DragonInteractor = mock(DragonInteractor::class.java)

    @Mock
    val mView: DragonView = mock(DragonView::class.java)

    @Mock
    val mListener: DragonInteractor.Callback = mock(DragonInteractor.Callback::class.java)

    @Mock
    val mDragonModel: DragonModel = mock(DragonModel::class.java)

    private lateinit var dragonList: List<DragonModel>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = DragonInteractorImpl(Dispatchers.Unconfined)
        mPresenter = DragonPresenterImpl(mView, mInteractor)
        presenter = DragonPresenterImpl(mView, interactor)

        dragonList = listOf(mDragonModel)
    }

    @Test
    fun `When response from API is successful then add dragons to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getDragons() } doReturn Response.success(dragonList)
        }

        presenter.getDragon(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            toggleSwipeRefresh(false)
            updateDragon(dragonList)
        }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getDragons()
            } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        interactor.getDragon(api = mockRepo, listener = mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getDragons()
            } doReturn Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        presenter.getDragon(mockRepo)

        verifyBlocking(mView) {
            showProgress()
            showError("Error: 404")
            toggleSwipeRefresh(false)
        }
    }

    @Test
    fun `When HttpException occurs`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getDragons()
            } doThrow HttpException(
                Response.error<Any>(
                    500,
                    "Test server error".toResponseBody("text/plain".toMediaTypeOrNull())
                )
            )
        }

        interactor.getDragon(api = mockRepo, listener = mListener)

        verifyBlocking(mListener) { onError("HTTP 500 Response.error()") }
    }

    @Test(expected = Throwable::class)
    fun `When job fails to execute`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getDragons()
            } doThrow Throwable()
        }

        interactor.getDragon(api = mockRepo, listener = mListener)
    }

    @Test
    fun `Cancel request`() {
        mPresenter.cancelRequest()

        Mockito.verify(mInteractor).cancelRequest()
    }

}