package com.example.sleeppc.ui.component.modifier

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalView
import kotlinx.coroutines.flow.collectLatest

private class ScaleIndicationInstance : IndicationInstance {
    val animatedScalePercent = Animatable(1f)

    suspend fun animateToPressed() {
        animatedScalePercent.animateTo(0.9f, spring())
    }

    suspend fun animateToResting() {
        animatedScalePercent.animateTo(1f, spring())
    }

    override fun ContentDrawScope.drawIndication() {
        scale(scale = animatedScalePercent.value) {
            this@drawIndication.drawContent()
        }
    }
}

// Singleton that can be reused
object ScaleIndication : Indication {
    @RequiresApi(Build.VERSION_CODES.R)
    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        val instance = remember(interactionSource) { ScaleIndicationInstance() }
        val view = LocalView.current
        val haptic = { it: Int -> view.performHapticFeedback(it)}

        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> {
                        haptic(HapticFeedbackConstants.KEYBOARD_PRESS)
                        instance.animateToPressed()
                    }
                    is PressInteraction.Release -> {
                        haptic(HapticFeedbackConstants.KEYBOARD_RELEASE)
                        instance.animateToResting()
                    }
                    is PressInteraction.Cancel -> {
                        haptic(HapticFeedbackConstants.TEXT_HANDLE_MOVE)
                        instance.animateToResting()
                    }
                }
            }
        }

        return instance
    }

    fun Modifier.scaleClick(onClick: () -> Unit): Modifier = composed { this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ScaleIndication
    ) { onClick() } }
}