package uz.gita.latizx.uz_eng.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkStateValidator @Inject constructor(@ApplicationContext private val context: Context) {
    var hasNetwork = false

    fun listenNetworkState(onAvailableBlock: () -> Unit, onUnavailable: () -> Unit) {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI) //from wifi
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)//from sim card
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                hasNetwork = false
                onUnavailable.invoke()
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                hasNetwork = true
                onAvailableBlock.invoke()
            }
        }

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}