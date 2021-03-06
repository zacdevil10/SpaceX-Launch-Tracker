package uk.co.zac_h.spacex.utils

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import retrofit2.Response
import retrofit2.mock.Calls
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

class BaseNetworkTest : BaseNetwork() {

    @Mock
    val mLaunchModel: LaunchesModel = mock(LaunchesModel::class.java)

    @Test
    fun `When make network call return response`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getLaunch("1")
            } doReturn Calls.response(Response.success(mLaunchModel))
        }

        mockRepo.getLaunch("1").makeCall {
            onResponseSuccess = { assert(mLaunchModel == it.body()) }
        }
    }

    @Test
    fun `When make network call return error`() {
        val mockRepo = mock<SpaceXInterface> {
            onBlocking {
                getLaunch("1")
            } doReturn Calls.response(
                Response.error(
                    404,
                    "{\\\"Error\\\":[\\\"404\\\"]}".toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
        }

        mockRepo.getLaunch("1").makeCall {
            onResponseFailure = { assert(it == "Error: 404") }
        }
    }
}