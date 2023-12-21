package com.example.sleeppc

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sleeppc.service.startSleepPcService
import com.example.sleeppc.service.startUnlockPcService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShortcutActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataString = intent.getStringExtra("shortcut_data")

        Log.d("!!!", "ShortcutActivity: $dataString")

        when (dataString) {
            "sleep_pc" -> {
//                sleepPC()
                startSleepPcService()
            }

//            "wake pc" -> {
//
//            }

            "unlock_pc" -> {
                startUnlockPcService()
            }

//            "wake and unlock pc" -> {
//
//            }
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            val wifiNetworkSpecifier = WifiNetworkSpecifier.Builder()
//                .setSsid("Luke 2.4G/5G")
//                .setWpa2Passphrase("00000800")
//                .build()
//
//            val networkRequest =
//                NetworkRequest.Builder()
//                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//                    .setNetworkSpecifier(wifiNetworkSpecifier)
//                    .build()
//
//            val connectivityManager =
//                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//            val networkCallback = object : ConnectivityManager.NetworkCallback() {
//                override fun onAvailable(network: Network) {
//                    // 你可以在這裡使用你的網路連接
//                    connectivityManager.bindProcessToNetwork(network)
//                    Log.d("!!!", "onAvailable: ")
//                    this@ShortcutActivity.finish()
//                }
//            }
//
//            connectivityManager.requestNetwork(networkRequest, networkCallback)
//
//        }

        lifecycleScope.launch(Dispatchers.Default) {
            delay(10000)
        }
            finish()
    }
}




