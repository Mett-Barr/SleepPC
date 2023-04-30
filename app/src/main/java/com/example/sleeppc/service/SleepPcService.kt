package com.example.sleeppc.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import com.example.sleeppc.network.NsdHelper
import com.example.sleeppc.network.RetrofitClient
import com.example.sleeppc.pccontrol.sleepPC
import com.example.sleeppc.tool.ActionType
import com.example.sleeppc.tool.bindProcessToWifi
import com.example.sleeppc.tool.createNotification
import com.example.sleeppc.tool.vibrate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SleepPcService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("!!!", "SleepPcService")

        // 啟動前台通知
        startForeground(1, createNotification(ActionType.SLEEP_PC))

        // 在 Service 中執行你的任務

        val nsd = NsdHelper(this)

        nsd.discoverServices {
            CoroutineScope(Dispatchers.IO).launch {

                bindProcessToWifi()

                RetrofitClient(this@SleepPcService, it).sleepPc()
                stopSelf()
            }
        }

        Log.d("!!!", "onStartCommand: ")

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

//    private fun createNotification(): Notification {
//        // 創建前台通知
//        val channelId = "my_channel"
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setContentTitle("My Service")
//            .setContentText("Service is running in the background")
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setCategory(NotificationCompat.CATEGORY_SERVICE)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "My Service Channel",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
//        }
//        return notificationBuilder.build()
//    }
}

fun Context.startSleepPcService() {
    val intent = Intent(this, SleepPcService::class.java)
    startService(intent)

    vibrate()
}