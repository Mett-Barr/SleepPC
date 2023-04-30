package com.example.sleeppc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.sleeppc.pccontrol.sleepPC
import com.example.sleeppc.service.startSleepPcService

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


//        startSleepPcService()

        sleepPC()
//        wakeOnLan() { finish() }

//        finish()


//        test()
    }
}

fun Context.test() {

    // 1
//    val intent = Intent().apply {
//        setClassName("com.easycard.wallet", "com.easycard.wallet.nonfc.activity.WelcomePageActivity")
//    }
//    startActivity(intent)

    // 2
    val intent = Intent("com.android.launcher.action.INSTALL_SHORTCUT").apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
        setClassName("com.easycard.wallet", "com.easycard.wallet.nonfc.activity.WelcomePageActivity")

        // 根据需要设置适当的 flags
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        // 如果有额外的信息，请在这里添加
        // putExtra("key", "value")
    }

    startActivity(intent)
}