package com.example.sleeppc.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.sleeppc.pccontrol.startUnlockActivity
import com.example.sleeppc.pccontrol.wakeOnLan
import com.example.sleeppc.tool.ActionType
import com.example.sleeppc.tool.createNotification
import com.example.sleeppc.tool.vibrate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class UnlockPcService : Service() {

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("!!!", "UnlockPcService")

        // 啟動前台通知
        startForeground(2, createNotification(ActionType.UNLOCK_PC))

        CoroutineScope(Dispatchers.IO).launch {

            // 1. wake on lan
            wakeOnLan()

            // 2. unlock PC
            startUnlockActivity()

            // 3. stop Service
            stopSelf()
        }


        return START_STICKY
//        return super.onStartCommand(intent, flags, startId)
    }
}

fun Context.startUnlockPcService() {
    val intent = Intent(this, UnlockPcService::class.java)
    startService(intent)

    vibrate()
}