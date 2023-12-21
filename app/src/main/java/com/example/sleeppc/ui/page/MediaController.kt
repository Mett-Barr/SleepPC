package com.example.sleeppc.ui.page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sleeppc.R
import com.example.sleeppc.network.MediaControllerClient
import com.example.sleeppc.network.NsdHelper
import com.example.sleeppc.tool.bindProcessToWifi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class VirtualKey() {
    PLAY_PAUSE(),
    VOLUME_UP(),
    VOLUME_DOWN(),
    NEXT(),
    PREVIOUS(),
    MUTE(),
    BRIGHTNESS_UP,
    BRIGHTNESS_DOWN,
    DISPLAY_OFF;

//    companion object {
//        fun fromString(action: String): VirtualKey? {
//            return try {
//                valueOf(action.uppercase(Locale.getDefault()))
//            } catch (e: IllegalArgumentException) {
//                null
//            }
//        }
//    }
}

@Preview
@Composable
fun MediaController() {
    val coroutineScope = rememberCoroutineScope()

    var isInWiFiMode = false
    var ip = ""
    val context = LocalContext.current
    val nsd = NsdHelper(LocalContext.current)
    LaunchedEffect(Unit) {
        nsd.discoverServices {
            ip = it
            context.bindProcessToWifi {
                isInWiFiMode = true
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        Row(
            Modifier
                .padding(bottom = 50.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_skip_previous),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable {
                        if (isInWiFiMode && ip.isNotBlank()) {
                            coroutineScope.launch(Dispatchers.IO) {
                                MediaControllerClient(ip).sendCommand(VirtualKey.PREVIOUS.name)
                            }
                        }
                    }
                    .shadow(elevation = 8.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_play_arrow),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable {
                        if (isInWiFiMode && ip.isNotBlank()) {
                            coroutineScope.launch(Dispatchers.IO) {
                                MediaControllerClient(ip).sendCommand(VirtualKey.PLAY_PAUSE.name)
                                Log.d("!!!", "MediaController: ")
                            }
                        } else {
                            Log.d("!!!", "isInWiFiMode $isInWiFiMode  ip.isNotBlank() = ${ip.isNotBlank()}")
                        }
                    }
                    .shadow(elevation = 8.dp))
            Card(shape = CircleShape, elevation = 8.dp) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_skip_next),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(56.dp) // 這將確保Icon和背景有相同的大小
//                            .shadow(elevation = 8.dp)
//                        .clip(CircleShape)
//                        .background(Color.Gray)
                        .clickable {
                            if (isInWiFiMode && ip.isNotBlank()) {
                                coroutineScope.launch(Dispatchers.IO) {
                                    MediaControllerClient(ip).sendCommand(VirtualKey.NEXT.name)
                                }
                            }
                        }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}