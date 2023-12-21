package com.example.sleeppc.tool

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

// Context 擴展函數
fun Context.bindProcessToWifi(afterConnect: () -> Unit = {}) {
    val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            connMgr.bindProcessToNetwork(network)
            afterConnect()
        }
    }

    connMgr.registerNetworkCallback(networkRequest, networkCallback)
}