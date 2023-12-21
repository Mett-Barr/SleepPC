package com.example.sleeppc.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import com.example.sleeppc.pccontrol.sendWakeOnLanPacket
import com.example.sleeppc.pccontrol.startUnlockActivity
import com.example.sleeppc.tool.ActionType
import com.example.sleeppc.tool.bindProcessToWifi
import com.example.sleeppc.tool.createNotification
import com.example.sleeppc.tool.vibrate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val WIFI_ACTION_START = "com.example.sleeppc.start"
const val WIFI_ACTION_DONE = "com.example.sleeppc.done"

class UnlockPcService : Service() {

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("!!!", "UnlockPcService")

        // 啟動前台通知
        startForeground(2, createNotification(ActionType.UNLOCK_PC))

        // 1. set WiFi BroadcastReceiver
        val customAction = WIFI_ACTION_DONE
        val intentFilter = IntentFilter(customAction)

        // 使用 lateinit 變量來存儲 customBroadcastReceiver 的引用
        lateinit var customBroadcastReceiver: BroadcastReceiver

        customBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == customAction) {
                    // 在此處執行您想要的操作
//                    CoroutineScope(Dispatchers.IO).launch {
//
//                        // 3. wake on lan
//                        wakeOnLan()
//
//                        // 4. unlock PC
//                        startUnlockActivity()
//
//                        // 5. stop Service
//                        unregisterReceiver(customBroadcastReceiver)
//                        stopSelf()
//                    }


                    // version 2
                    CoroutineScope(Dispatchers.IO).launch {
                        bindProcessToWifi {
                            CoroutineScope(Dispatchers.IO).launch {
                                sendWakeOnLanPacket()

                                startUnlockActivity()

                                unregisterReceiver(customBroadcastReceiver)
                                stopSelf()
                            }
                        }
                    }
                }
            }
        }
        registerReceiver(customBroadcastReceiver, intentFilter)

        // 2. send start connect WiFi intent
        sendBroadcast(Intent(WIFI_ACTION_START))


        CoroutineScope(Dispatchers.Default).launch {
            delay(10000)

            // if no response
            unregisterReceiver(customBroadcastReceiver)
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