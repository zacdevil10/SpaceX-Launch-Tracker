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
import uk.co.zac_h.spacex.launches.details.payloads.LaunchDetailsPayloadsContract
import uk.co.zac_h.spacex.launches.details.payloads.LaunchDetailsPayloadsInteractor
import uk.co.zac_h.spacex.launches.details.payloads.LaunchDetailsPayloadsPresenter
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface

class LaunchDetailsPayloadsTest {

    private lateinit var mPresenter: LaunchDetailsPayloadsContract.Presenter
    private lateinit var presenter: LaunchDetailsPayloadsContract.Presenter
    private lateinit var interactor: LaunchDetailsPayloadsContract.Interactor

    @Mock
    val mInteractor: LaunchDetailsPayloadsContract.Interactor =
        Mockito.mock(LaunchDetailsPayloadsContract.Interactor::class.java)

    @Mock
    val mView: LaunchDetailsPayloadsContract.View =
        Mockito.mock(LaunchDetailsPayloadsContract.View::class.java)

    @Mock
    val mListener: LaunchDetailsPayloadsContract.InteractorCallback =
        Mockito.mock(LaunchDetailsPayloadsContract.InteractorCallback::class.java)

    @Mock
    val mLaunchesExtendedModel: LaunchesExtendedModel =
        Mockito.mock(LaunchesExtendedModel::class.java)

    private val mLaunchesExtendedDocsModel: LaunchesExtendedDocsModel =
        LaunchesExtendedDocsModel(listOf(mLaunchesExtendedModel))

    private lateinit var query: QueryModel

    private lateinit var mockRepoSuccess: SpaceXInterface
    private lateinit var mockRepoError: SpaceXInterface

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = LaunchDetailsPayloadsInteractor()
        mPresenter = LaunchDetailsPayloadsPresenter(mView, mInteractor)
        presenter = LaunchDetailsPayloadsPresenter(mView, interactor)

        val populateList = listOf(
            QueryPopulateModel("payloads", populate = "", select = "")
        )

        query = QueryModel(
            QueryLaunchesQueryModel("id"),
            QueryOptionsModel(false, populateList, "", listOf("payloads"), 1000)
        )

        mockRepoSuccess = mock<SpaceXInterface> {
            onBlocking { getQueriedLaunches(query) } doReturn Calls.response(
                Response.success(
                    mLaunchesExtendedDocsModel
                )
            )
        }

        mockRepoError = mock {
            onBlocking { getQueriedLaunches(query) } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }
    }

    @Test
    fun `When launch id is provided then get launch cores data`() {
        interactor.getPayloads("id", mockRepoSuccess, mListener)

        verifyBlocking(mListener) {
            onSuccess(mLaunchesExtendedDocsModel)
        }
    }

    @Test
    fun `Add launch data to view`() {
        presenter.getLaunch("id", mockRepoSuccess)

        verifyBlocking(mView) {
            showProgress()
            hideProgress()
            mLaunchesExtendedDocsModel.docs[0].payloads?.let {
                updatePayloadsRecyclerView(it)
            }
        }
    }

    @Test
    fun `When response from API is unsuccessful`() {
        interactor.getPayloads("id", mockRepoError, mListener)

        verifyBlocking(mListener) { onError("Error: 404") }
    }

    @Test
    fun `Show error in view when response from API fails`() {
        presenter.getLaunch("id", mockRepoError)

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