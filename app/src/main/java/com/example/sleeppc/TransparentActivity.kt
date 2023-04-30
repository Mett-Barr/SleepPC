package com.example.sleeppc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sleeppc.service.SleepPcService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TransparentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transparent)

        val intent = Intent(this, SleepPcService::class.java)
        startService(intent)

        CoroutineScope(Dispatchers.Default).launch {
            delay(3000)
            finish()
        }


        Log.d("!!!", "onCreate: ")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("!!!", "onDestroy: ")
    }
}