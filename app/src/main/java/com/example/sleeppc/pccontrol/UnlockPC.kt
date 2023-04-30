package com.example.sleeppc.pccontrol

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity

fun ComponentActivity.unlock() {
    startActivity(unlockIntent())
    finish()
}

fun Context.startUnlockActivity() {
    startActivity(unlockIntent())
}


fun unlockIntent(): Intent {
    val action = "com.example.mircius.fingerprintauth.shortcuts.UNLOCK"
    val packageName = "ro.andreimircius.remotefingerauth"
    val className = "com.example.mircius.fingerprintauth.UnlockWidgetActivity"

    val intent = Intent(action)
    intent.setClassName(packageName, className)

//    android.util.AndroidRuntimeException: Calling startActivity() from outside of an Activity
//    context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)


    return intent
}
