package com.example.sleeppc.ui.page

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.sleeppc.R
import com.example.sleeppc.network.MediaControllerClient
import com.example.sleeppc.network.NsdHelper
import com.example.sleeppc.tool.bindProcessToWifi
import com.example.sleeppc.ui.component.Wrapper
import com.example.sleeppc.ui.component.modifier.ScaleIndication
import com.example.sleeppc.ui.component.modifier.ScaleIndication.scaleClick
import com.example.sleeppc.ui.component.modifier.myshadowElevation
import com.example.sleeppc.ui.component.modifier.mytonalElevation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Controller() {

    val coroutineScope = rememberCoroutineScope()
    var isInWiFiMode by remember { mutableStateOf(false) }
    var ip by remember { mutableStateOf("") }
    fun isConnected() = isInWiFiMode && ip.isNotBlank()
    val client = remember(ip) { MediaControllerClient(ip) }

    fun inIO(fn: suspend () -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            fn()
        }
    }

    fun sendCommand(action: VirtualKey) {
        if (isConnected()) {
            inIO {
                client.sendCommand(action.name, onError = { Log.d("!!!", "onError: ") })
            }
        }
    }


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


    Box(modifier = Modifier.fillMaxSize().padding(WindowInsets.systemBars.asPaddingValues()).padding(bottom = 32.dp)) {
        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .padding(8.dp)
            ) {
                Surface(
                    modifier = Modifier.width(IntrinsicSize.Min),
                    shape = CircleShape,
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
                ) {
                    Column(Modifier.padding(8.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_volume_up),
                            contentDescription = "",
                            modifier = Modifier
                                .scaleClick { sendCommand(VirtualKey.VOLUME_UP) }
                                .size(36.dp)
                        )
                        Divider(
                            modifier = Modifier.clip(CircleShape).padding(vertical = 8.dp),
                            thickness = 2.dp,
                            color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_volume_down),
                            contentDescription = "",
                            modifier = Modifier.scaleClick { sendCommand(VirtualKey.VOLUME_DOWN) }
                                .size(36.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                Surface(
                    modifier = Modifier.width(IntrinsicSize.Min).aspectRatio(1f).fillMaxSize(),
                    shape = CircleShape,
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.aspectRatio(1f).fillMaxSize().padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_volume_off),
                            contentDescription = "",
                            modifier = Modifier
                                .scaleClick { sendCommand(VirtualKey.MUTE) }
                                .size(36.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            MyCard(onClick = { sendCommand(VirtualKey.PREVIOUS) }, shape = CircleShape) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_skip_previous),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(48.dp) // 這將確保Icon和背景有相同的大小
                )
            }
            Spacer(modifier = Modifier.weight(1.5f))
            MyCard(
                onClick = {
                    Log.d("!!!", "Inside onClick: Before calling sendCommand.")
                    sendCommand(VirtualKey.PLAY_PAUSE)
                    Log.d("!!!", "Inside onClick: After calling sendCommand.")
                },
                shape = CircleShape,
                isMainColor = true
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_play_pause),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(64.dp) // 這將確保Icon和背景有相同的大小
                )
            }
            Spacer(modifier = Modifier.weight(1.5f))
            MyCard(
                onClick = { sendCommand(VirtualKey.NEXT) },
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_skip_next),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(48.dp) // 這將確保Icon和背景有相同的大小
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .padding(8.dp)
            ) {
                Surface(
                    modifier = Modifier.width(IntrinsicSize.Min),
                    shape = CircleShape,
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
                ) {
                    Column(Modifier.padding(8.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_brightness_high),
                            contentDescription = "",
                            modifier = Modifier
                                .scaleClick { sendCommand(VirtualKey.BRIGHTNESS_UP) }
                                .size(36.dp)
                        )
                        Divider(
                            Modifier
                                .clip(CircleShape)
                                .padding(vertical = 8.dp),
                            thickness = 2.dp,
                            color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_brightness_low),
                            contentDescription = "",
                            modifier = Modifier
                                .scaleClick { sendCommand(VirtualKey.BRIGHTNESS_DOWN) }
                                .size(36.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                Surface(
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .aspectRatio(1f)
                        .fillMaxSize(),
                    shape = CircleShape,
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_turn_off_screen),
                            contentDescription = "",
                            modifier = Modifier
                                .scaleClick { sendCommand(VirtualKey.DISPLAY_OFF) }
                                .size(36.dp)
                        )
                    }
                }
            }

        }
    }
}

@Composable
@NonRestartableComposable
fun MyCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RectangleShape,
    isMainColor: Boolean = false,
//    color: Color = MaterialTheme.colorScheme.secondaryContainer,
//    contentColor: Color = contentColorFor(color),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) = Wrapper(
    Modifier.clickable(
        interactionSource = interactionSource,
        indication = ScaleIndication,
        enabled = enabled,
        onClick = onClick
    )
) {

    val tonalElevation by mytonalElevation(enabled, interactionSource)
    val shadowElevation by myshadowElevation(enabled, interactionSource)

    val color: Color =
        if (isMainColor) MaterialTheme.colorScheme.surfaceColorAtElevation(tonalElevation * 5) else MaterialTheme.colorScheme.surfaceColorAtElevation(
            tonalElevation
        )
    val contentColor: Color = contentColorFor(color)
//    val color: Color = if (isMainColor) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
//    val contentColor: Color = contentColorFor(color)

    val absoluteElevation = LocalAbsoluteTonalElevation.current + tonalElevation
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalAbsoluteTonalElevation provides absoluteElevation
    ) {
        Box(
            modifier = modifier
                .minimumInteractiveComponentSize()
                .surface(
                    shape = shape,
                    backgroundColor = surfaceColorAtElevation(
                        color = color,
                        elevation = absoluteElevation
                    ),
                    border = border,
                    shadowElevation = shadowElevation
                ),
            propagateMinConstraints = true
        ) { content() }
    }
}

internal fun Modifier.surface(
    shape: Shape,
    backgroundColor: Color,
    border: BorderStroke?,
    shadowElevation: Dp
) = this
    .shadow(shadowElevation, shape, clip = false)
    .then(if (border != null) Modifier.border(border, shape) else Modifier)
    .background(color = backgroundColor, shape = shape)
    .clip(shape)

@Composable
internal fun surfaceColorAtElevation(color: Color, elevation: Dp): Color {
    return if (color == MaterialTheme.colorScheme.surface) {
        MaterialTheme.colorScheme.surfaceColorAtElevation(elevation)
    } else {
        color
    }
}

