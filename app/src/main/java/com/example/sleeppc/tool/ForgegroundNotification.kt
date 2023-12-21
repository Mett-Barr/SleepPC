package com.example.sleeppc.tool

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.sleeppc.R

enum class ActionType {
    SLEEP_PC, UNLOCK_PC
}

fun Context.createNotification(type: ActionType): Notification {
    // 創建前台通知
    return when(type) {
        ActionType.SLEEP_PC -> createNotification(
            contentTitle = "Sleep PC"
        )

        ActionType.UNLOCK_PC -> createNotification(
            contentTitle = "Unlock PC",
        )
    }
}

fun Context.createNotification(
    channelId: String = "my_channel",
    contentTitle: String = "My Service",
    contentText: String = "Service is running in the background",
    priority: Int = NotificationCompat.PRIORITY_HIGH,
    category: String = NotificationCompat.CATEGORY_SERVICE,
    channelName: String = "My Service Channel"
): Notification {
    val notificationBuilder = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.drawable.ic_computer_light_mode)
        .setContentTitle(contentTitle)
        .setContentText(contentText)
        .setPriority(priority)
        .setCategory(category)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }
    return notificationBuilder.build()
}
