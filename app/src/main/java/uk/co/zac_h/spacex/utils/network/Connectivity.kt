package uk.co.zac_h.spacex.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

@Suppress("DEPRECATION")
fun isConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT < 23) {
        val networkInfo = connectivityManager.activeNetworkInfo

        networkInfo?.let {
            return it.isConnected && (it.type == ConnectivityManager.TYPE_WIFI || it.type == ConnectivityManager.TYPE_MOBILE)
        }
    } else {
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        networkCapabilities?.let {
            return (it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || it.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            ))
        }
    }
    return false
}
