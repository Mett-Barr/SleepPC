package com.example.sleeppc.pccontrol

import android.app.Service
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.sleeppc.network.NsdHelper
import com.example.sleeppc.network.RetrofitClient
import com.example.sleeppc.service.SleepPcService
import com.example.sleeppc.tool.bindProcessToWifi
import com.example.sleeppc.tool.vibrate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ComponentActivity.sleepPC() {
    val intent = Intent(this, SleepPcService::class.java)
    startService(intent)

    vibrate()

    Log.d("!!!", "sleepPC: ")
    finish()
}

//fun Service.sleepPC() {
//    val nsd = NsdHelper(this)
//
//    nsd.discoverServices {
//        CoroutineScope(Dispatchers.IO).launch {
//
//            bindProcessToWifi()
//
//            RetrofitClient(this@sleepPC, it).sleepPc()
//            stopSelf()
//        }
//    }
//}