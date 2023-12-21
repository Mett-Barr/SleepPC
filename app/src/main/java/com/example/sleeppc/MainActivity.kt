package com.example.sleeppc

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.sleeppc.network.connectToKnownWifi
import com.example.sleeppc.ui.page.Controller
import com.example.sleeppc.ui.theme.M3Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            M3Theme {
                Surface(
                    Modifier
//                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize(),
//                        .padding(WindowInsets.systemBars.asPaddingValues()),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Controller()
//                    MediaController()
                }
            }
        }


//        startSleepPcService()

//        sleepPC()
//        wakeOnLan() { finish() }

//        finish()


//        test()

//        requestLocationPermission()

//        requestWriteSettingsPermission()
    }


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            Log.d("!!!", "requestPermissionLauncher: ")
            if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    CoroutineScope(Dispatchers.IO).launch {
                        connectToKnownWifi(this@MainActivity)
//                        connectToKnownWifiLegacy(this@MainActivity)
//                        connectToSavedWifi(this@MainActivity)
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Location permission is required to connect to Wi-Fi.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun requestLocationPermission() {
        Log.d("!!!", "requestLocationPermission: ")
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            )
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                CoroutineScope(Dispatchers.IO).launch {
                    connectToKnownWifi(this@MainActivity)
//                    connectToKnownWifiLegacy(this@MainActivity)
//                    connectToSavedWifi(this@MainActivity)
                }
            }
        }
    }


    private val writeSettingsActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("!!!", "writeSettingsActivityResultLauncher")
            if (Settings.System.canWrite(this)) {
                // WRITE_SETTINGS permission granted, proceed to request location permission
                requestLocationPermission()
            } else {
                Toast.makeText(
                    this,
                    "WRITE_SETTINGS permission is required to connect to Wi-Fi.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun requestWriteSettingsPermission() {
        Log.d("!!!", "requestWriteSettingsPermission: ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                writeSettingsActivityResultLauncher.launch(intent)
            } else {
                // If WRITE_SETTINGS permission is already granted, proceed to request location permission
                requestLocationPermission()
            }
        }
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
        setClassName(
            "com.easycard.wallet",
            "com.easycard.wallet.nonfc.activity.WelcomePageActivity"
        )

        // 根据需要设置适当的 flags
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        // 如果有额外的信息，请在这里添加
        // putExtra("key", "value")
    }

    startActivity(intent)
}