package com.example.sleeppc

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.sleeppc.network.NsdHelper
import com.example.sleeppc.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SleepPcService : Service() {


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 啟動前台通知
        startForeground(1, createNotification())

        // 在 Service 中執行你的任務
//        CoroutineScope(Dispatchers.Default).launch {
//
//            while (true) {
//
//                Log.d("SleepPcService", "Test")
//                delay(1500)
//            }
//        }



        val nsd = NsdHelper(this)

        nsd.discoverServices {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient(this@SleepPcService, it).sleepPc()
                stopSelf()
            }
        }

//        NsdHelper(this).discoverServices() {
//            RetrofitClient(it).sleepPc()
//        }


        Log.d("!!!", "onStartCommand: ")


        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(): Notification {
        // 創建前台通知
        val channelId = "my_channel"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("My Service")
            .setContentText("Service is running in the background")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "My Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
        return notificationBuilder.build()
    }
}