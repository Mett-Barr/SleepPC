package com.example.sleeppc.tool

import android.app.Service
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

fun Context.vibrate() {
    val t: LongArray = longArrayOf(0, 2, 115, 2)
    val a: IntArray = intArrayOf(0, 185, 0, 230)

    val vibrator = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createWaveform(t, a, -1))
    }
}