package com.example.sleeppc

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.os.VibrationEffect
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sleeppc.network.NsdHelper
import com.example.sleeppc.network.RetrofitClient
import com.example.sleeppc.ui.theme.SleepPCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContent {
//
//            var text by remember {
//                mutableStateOf("init...")
//            }
//
//            SleepPCTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
////                    Greeting("Android")
//                    Text(text = text)
//                }
//            }
//
//
//
//            val nsd = NsdHelper(this)
//            nsd.discoverServices {
//                text = it
//            }
//
//            if (text != "init...") {
////                RetrofitClient(text).sleepPc()
//            }
//        }

        val intent = Intent(this, SleepPcService::class.java)
        startService(intent)

        val t: LongArray = longArrayOf(0, 2, 115, 2)
        val a: IntArray = intArrayOf(0, 185, 0, 230)

        val vibrator = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(t, a, -1))
        }


        finish()
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SleepPCTheme {
        Greeting("Android")
    }
}