package com.example.sleeppc.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.sleeppc.R
import com.example.sleeppc.network.KtorClient
import com.example.sleeppc.network.NsdHelper
import com.example.sleeppc.tool.bindProcessToWifi
import com.example.sleeppc.tool.vibrate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SleepPcService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("!!!", "SleepPcService")

        // 啟動前台通知
//        startForeground(1, createNotification(ActionType.SLEEP_PC))
        createAndShowForegroundNotification(1)

        // 在 Service 中執行你的任務

        val nsd = NsdHelper(this)

        nsd.discoverServices {
            bindProcessToWifi {
//                CoroutineScope(Dispatchers.IO).launch {
//                    // Retrofit Version
//                    RetrofitClient(this@SleepPcService, it).getServerStatus()
//
//                    RetrofitClient(this@SleepPcService, it).sleepPc()
//                    withContext(Dispatchers.Main) {
//                        Toast.makeText(this@SleepPcService, "Sleep complete", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                    stopSelf()
//                }

                // Ktor Version
                CoroutineScope(Dispatchers.IO).launch {
                    KtorClient(it).sleepPc(
                        onSuccess = {
                            Toast.makeText(
                                this@SleepPcService,
                                "Sleep complete",
                                Toast.LENGTH_SHORT
                            ).show()
                            stopSelf()
                        },
                        onError = {
                            Toast.makeText(this@SleepPcService, "Sleep Failure", Toast.LENGTH_SHORT)
                                .show()
                        }
                    )
                }
            }
        }

        Log.d("!!!", "onStartCommand: ")

        return START_NOT_STICKY
//        return START_STICKY
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


    private fun createAndShowForegroundNotification(notificationId: Int) {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "com.example.your_app.notification.CHANNEL_ID_FOREGROUND"
            val importance = NotificationManager.IMPORTANCE_LOW
            prepareChannel(channelId, importance)
            NotificationCompat.Builder(this, channelId)
        } else {
            NotificationCompat.Builder(this)
        }
        builder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_computer_light_mode)
            .setContentTitle("PC Sleep")
            .setContentText("Command is running.")

        val notification = builder.build()
        startForeground(notificationId, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prepareChannel(channelId: String, importance: Int) {
        val appName = getString(R.string.app_name)
        val description = "description"
        val nm = getSystemService(NotificationManager::class.java)
        var channel = nm.getNotificationChannel(channelId)
        if (channel == null) {
            channel = NotificationChannel(channelId, appName, importance).apply {
                this.description = description
            }
            nm.createNotificationChannel(channel)
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.startSleepPcService() {
    val intent = Intent(this, SleepPcService::class.java)
//    startService(intent)
    startForegroundService(intent)

    vibrate()
}