package com.example.sleeppc.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.*
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

//suspend fun connectToKnownWifi(context: Context, ssid: String) {
//    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//
//    if (!wifiManager.isWifiEnabled) {
//        wifiManager.isWifiEnabled = true
//    }
//
//    val confList = wifiManager.configuredNetworks
//    val networkId = confList.find { it.SSID == "\"$ssid\"" }?.networkId
//
//    if (networkId != null) {
//        withContext(Dispatchers.IO) {
//            wifiManager.disconnect()
//            wifiManager.enableNetwork(networkId, true)
//            wifiManager.reconnect()
//        }
//    } else {
//        // The specified SSID is not in the list of configured networks.
//    }
//}

//@RequiresApi(Build.VERSION_CODES.Q)
//suspend fun connectToKnownWifi(context: Context, ssid: String) {
//    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    val networkRequest = NetworkRequest.Builder()
//        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//        .build()
//
//    val wifiNetworkCallback = object : ConnectivityManager.NetworkCallback() {
//        override fun onAvailable(network: Network) {
//            super.onAvailable(network)
//            connectivityManager.bindProcessToNetwork(network)
//        }
//    }
//
//    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//    val confList = wifiManager.configuredNetworks
//    val targetSsid = confList.find { it.SSID == "\"$ssid\"" }
//
//    if (targetSsid != null) {
//        withContext(Dispatchers.IO) {
//            connectivityManager.requestNetwork(networkRequest, wifiNetworkCallback)
//            wifiManager.startRestrictiveConnection(targetSsid)
//        }
//    } else {
//        // The specified SSID is not in the list of configured networks.
//    }
//}

const val WIFI_2_4G = "\\u912d\\u5c0f\\u9686\\u0032\\u002e\\u0034\\u0047"
//const val WIFI_2_4G = "鄭小隆2.4G"
const val WIFI_5G = "鄭小隆5G"
const val WIFI_TEST = "Pixel_1455"

@RequiresApi(Build.VERSION_CODES.Q)
fun connectToKnownWifi(context: Context, ssid: String = WIFI_2_4G, onSuccess: () -> Unit = {}) {
    Log.d("!!!", "connectToKnownWifi: ")

    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .setNetworkSpecifier(
            WifiNetworkSpecifier.Builder()
                .setSsid(ssid)
//                .setSsidPattern(PatternMatcher(ssid, PatternMatcher.PATTERN_LITERAL))
                .build()
        )
        .build()

    val wifiNetworkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            connectivityManager.bindProcessToNetwork(network)
            Log.d("!!!", "onAvailable: ")
            onSuccess()
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Log.d("!!!", "onUnavailable: ")
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            Log.d("!!!", "onLosing: ")
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Log.d("!!!", "onLost: ")
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            Log.d("!!!", "onCapabilitiesChanged: ")
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
            Log.d("!!!", "onLinkPropertiesChanged: ")
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            super.onBlockedStatusChanged(network, blocked)
            Log.d("!!!", "onBlockedStatusChanged: ")
        }
    }

    connectivityManager.requestNetwork(networkRequest, wifiNetworkCallback)

    Log.d("!!!", "connectToKnownWifi: end")
}

@SuppressLint("MissingPermission")
fun connectToKnownWifiLegacy(context: Context, ssid: String = WIFI_2_4G) {

    listSavedWifiSSIDs(context)

    try {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiConfigurations = wifiManager.configuredNetworks

        for (wifiConfiguration in wifiConfigurations) {
            if (wifiConfiguration.SSID == "\"$ssid\"") {
                wifiManager.enableNetwork(wifiConfiguration.networkId, true)
                Log.i("!!!", "connectToKnownWifiLegacy: will enable ${wifiConfiguration.SSID}")
                wifiManager.reconnect()
                return
            }
        }
    } catch (e: NullPointerException) {
        Log.e("!!!", "connectToKnownWifiLegacy: Missing network configuration.", e)
    } catch (e: IllegalStateException) {
        Log.e("!!!", "connectToKnownWifiLegacy: Missing network configuration.", e)
    }
}

@SuppressLint("MissingPermission")
fun listSavedWifiSSIDs(context: Context) {
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiConfigurations = wifiManager.configuredNetworks

    Log.i("SavedWifiSSIDs", "List of saved Wi-Fi SSIDs: ${wifiConfigurations.size}")
    for (wifiConfiguration in wifiConfigurations) {
        Log.i("SavedWifiSSIDs", "SSID: ${wifiConfiguration.SSID}")
    }
}

@SuppressLint("MissingPermission")
fun connectToSavedWifi(context: Context, targetSsid: String = WIFI_2_4G) {
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiConfigurations = wifiManager.configuredNetworks

    // Ensure Wi-Fi is enabled
    if (!wifiManager.isWifiEnabled) {
        wifiManager.isWifiEnabled = true
    }

    // Iterate through the list of saved Wi-Fi networks
    for (wifiConfiguration in wifiConfigurations) {
        if (wifiConfiguration.SSID == "\"$targetSsid\"") {
            // Disconnect from the current Wi-Fi network
            wifiManager.disconnect()

            // Enable and reconnect to the target Wi-Fi network
            wifiManager.enableNetwork(wifiConfiguration.networkId, true)
            wifiManager.reconnect()

            Log.i("ConnectToSavedWifi", "Connecting to: ${wifiConfiguration.SSID}")
            break
        }
    }
}
