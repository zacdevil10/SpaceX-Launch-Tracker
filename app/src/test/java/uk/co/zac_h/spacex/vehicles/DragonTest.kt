package uk.co.zac_h.spacex.vehicles

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
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
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.mock.Calls
import uk.co.zac_h.spacex.model.spacex.DragonModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.dragon.DragonContract
import uk.co.zac_h.spacex.vehicles.dragon.DragonInteractorImpl
import uk.co.zac_h.spacex.vehicles.dragon.DragonPresenterImpl

class DragonTest {

    private lateinit var mPresenter: DragonContract.DragonPresenter
    private lateinit var presenter: DragonContract.DragonPresenter
    private lateinit var interactor: DragonContract.DragonInteractor

    @Mock
    val mInteractor: DragonContract.DragonInteractor =
        mock(DragonContract.DragonInteractor::class.java)

    @Mock
    val mView: DragonContract.DragonView = mock(DragonContract.DragonView::class.java)

    @Mock
    val mListener: DragonContract.InteractorCallback =
        mock(DragonContract.InteractorCallback::class.java)

    @Mock
    val mDragonModel: DragonModel = mock(DragonModel::class.java)

    private lateinit var dragonList: List<DragonModel>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = DragonInteractorImpl()
        mPresenter = DragonPresenterImpl(mView, mInteractor)
        presenter = DragonPresenterImpl(mView, interactor)

        dragonList = listOf(mDragonModel)
    }

    @Test
    fun `When response from API is successful then add dragons to view`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking { getDragons() } doReturn Calls.response(Response.success(dragonList))
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
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
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
            } doReturn Calls.response(
                Response.error(
                404,
                "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
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