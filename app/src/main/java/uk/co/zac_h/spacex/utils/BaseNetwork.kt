package uk.co.zac_h.spacex.utils

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

open class BaseNetwork {

    fun <T> Call<T>.makeCall(callback: CallBackKt<T>.() -> Unit) {
        val callBackKt = CallBackKt<T>()
        callback.invoke(callBackKt)
        this.enqueue(callBackKt)
    }

    class CallBackKt<T> : Callback<T> {

        var onResponseSuccess: ((Response<T>) -> Unit)? = null
        var onResponseFailure: ((error: String) -> Unit)? = null

        private var canceled = false

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (!canceled) {
                if (response.isSuccessful) {
                    onResponseSuccess?.invoke(response)
                } else {
                    onResponseFailure?.invoke("Error: ${response.code()}")
                }
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            if (!canceled) when (t) {
                is HttpException ->
                    onResponseFailure?.invoke(
                        t.localizedMessage ?: "There was a network error! Please try refreshing."
                    )
                is UnknownHostException ->
                    onResponseFailure?.invoke(
                        "Unable to resolve host! Check your network connection and try again."
                    )
                else ->
                    Log.e(
                        this.javaClass.name,
                        t.localizedMessage ?: "Job failed to execute"
                    )
            }
        }

        fun cancel() {
            canceled = true
        }
    }

}