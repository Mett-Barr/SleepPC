package com.example.sleeppc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.sleeppc.pccontrol.sleepPC
import com.example.sleeppc.service.startSleepPcService
import com.example.sleeppc.service.startUnlockPcService

class ShortcutActivity : AppCompatActivity() {
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

        finish()
    }
}




