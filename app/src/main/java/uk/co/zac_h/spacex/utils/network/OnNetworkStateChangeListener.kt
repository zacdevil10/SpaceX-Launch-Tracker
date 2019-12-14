package uk.co.zac_h.spacex.utils.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build

class OnNetworkStateChangeListener(private val context: Context?) {

    private val listeners: MutableSet<NetworkStateReceiverListener> = HashSet()

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    private var broadcastReceiver: BroadcastReceiver? = null
    private var intentFilter: IntentFilter? = null

    private var connected: Boolean? = null

    interface NetworkStateReceiverListener {
        fun networkAvailable() {}
        fun networkLost() {}
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context?.let { context ->
                connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                networkCallback = object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        println("Network available")
                        connected = true
                        notifyStateToAll()
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        println("Network connection lost")
                        connected = false
                        notifyStateToAll()
                    }
                }
            }
        } else {
            broadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent?) {
                    if (intent == null || intent.extras == null) return

                    connected = isConnected(context)

                    notifyStateToAll()
                }
            }

            @Suppress("DEPRECATION")
            intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        }
    }

    private fun notifyStateToAll() {
        for (listener in listeners) {
            notifyState(listener)
        }
    }

    private fun notifyState(listener: NetworkStateReceiverListener?) {
        if (connected == null || listener == null) {
            return
        }

        connected?.let {
            if (it) {
                listener.networkAvailable()
            } else {
                listener.networkLost()
            }
        }
    }

    fun updateState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) context?.let {
            connected = isConnected(context)

            notifyStateToAll()
        }
    }

    fun registerReceiver() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
            context?.registerReceiver(broadcastReceiver, intentFilter)
        else connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    fun unregisterReceiver() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
            context?.unregisterReceiver(broadcastReceiver)
        else connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    fun addListener(listener: NetworkStateReceiverListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: NetworkStateReceiverListener) {
        listeners.remove(listener)
    }
}