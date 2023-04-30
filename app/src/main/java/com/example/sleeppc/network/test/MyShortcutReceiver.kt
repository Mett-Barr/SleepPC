package com.example.sleeppc.network.test

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class MyShortcutReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val action = intent.action
        if (Intent.ACTION_CREATE_SHORTCUT == action) {
            // 通过 getIntent() 获取原始 intent
            val originalIntent = intent.getParcelableExtra<Intent>(Intent.EXTRA_SHORTCUT_INTENT)

            // 分析原始 intent 的内容
            if (originalIntent != null) {
                val originalAction = originalIntent.action
                val originalData = originalIntent.dataString

                // 在此处处理原始 intent 的相关信息
                Log.d("!!!", originalIntent.toString())
            }
        }
    }
}
