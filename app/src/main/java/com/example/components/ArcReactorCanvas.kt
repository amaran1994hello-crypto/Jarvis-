package com.example.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.example.ui.theme.JarvisCyan
import com.example.ui.theme.JarvisCyanGlow
import com.example.ui.theme.JarvisGold
import com.example.ui.theme.JarvisGoldGlow
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ArcReactorCanvas(
    modifier: Modifier = Modifier,
    amplitude: Float = 0f,
    isListening: Boolean = false,
    isSpeaking: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ArcReactorRotation")

    val rotationSlow by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SlowRotation"
    )

    val rotationFast by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "FastRotation"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isListening || isSpeaking) 800 else 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    Box(
        modifier = modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val baseRadius = (size.minDimension / 2f) * 0.85f * pulseScale
            val dynamicRadius = baseRadius + (amplitude * 24.dp.toPx())

            // Background ambient glow radial gradient
            val glowBrush = Brush.radialGradient(
                colors = listOf(
                    if (isSpeaking) JarvisCyanGlow else if (isListening) JarvisGoldGlow else JarvisCyanGlow,
                    Color.Transparent
                ),
                center = center,
                radius = dynamicRadius * 1.3f
            )
            drawCircle(brush = glowBrush, radius = dynamicRadius * 1.3f, center = center)

            // Outer segmented ring
            rotate(rotationSlow, pivot = center) {
                drawCircle(
                    color = JarvisCyan.copy(alpha = 0.35f),
                    radius = dynamicRadius,
                    center = center,
                    style = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 18f), 0f)
                    )
                )

                // 8 Arc nodes on outer ring
                for (i in 0 until 8) {
                    val angle = Math.toRadians((i * 45).toDouble())
                    val nodeX = center.x + (dynamicRadius * cos(angle)).toFloat()
                    val nodeY = center.y + (dynamicRadius * sin(angle)).toFloat()
                    drawCircle(
                        color = JarvisGold,
                        radius = 3.dp.toPx(),
                        center = Offset(nodeX, nodeY)
                    )
                }
            }

            // Middle counter-rotating ring
            rotate(rotationFast, pivot = center) {
                drawCircle(
                    color = JarvisGold.copy(alpha = 0.5f),
                    radius = dynamicRadius * 0.78f,
                    center = center,
                    style = Stroke(
                        width = 3.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 25f), 10f)
                    )
                )

                // Triangle markers in center
                for (i in 0 until 6) {
                    val angle = Math.toRadians((i * 60).toDouble())
                    val r1 = dynamicRadius * 0.65f
                    val r2 = dynamicRadius * 0.76f
                    val x1 = center.x + (r1 * cos(angle)).toFloat()
                    val y1 = center.y + (r1 * sin(angle)).toFloat()
                    val x2 = center.x + (r2 * cos(angle)).toFloat()
                    val y2 = center.y + (r2 * sin(angle)).toFloat()

                    drawLine(
                        color = JarvisCyan,
                        start = Offset(x1, y1),
                        end = Offset(x2, y2),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }

            // Core inner energy ring
            drawCircle(
                color = JarvisCyan,
                radius = dynamicRadius * 0.52f,
                center = center,
                style = Stroke(width = 3.5.dp.toPx())
            )

            // Dynamic Audio Waveform spikes around center core
            val barCount = 24
            for (i in 0 until barCount) {
                val angle = Math.toRadians((i * (360.0 / barCount)).toDouble())
                val spikeHeight = (4.dp.toPx() + (amplitude * 16.dp.toPx() * (if (i % 2 == 0) 1.2f else 0.8f)))
                val startR = dynamicRadius * 0.38f
                val endR = startR + spikeHeight
                val sx = center.x + (startR * cos(angle)).toFloat()
                val sy = center.y + (startR * sin(angle)).toFloat()
                val ex = center.x + (endR * cos(angle)).toFloat()
                val ey = center.y + (endR * sin(angle)).toFloat()

                drawLine(
                    color = if (isSpeaking) JarvisCyan else if (isListening) JarvisGold else JarvisCyan,
                    start = Offset(sx, sy),
                    end = Offset(ex, ey),
                    strokeWidth = 2.dp.toPx()
                )
            }

            // Central Arc Core Bulb
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White,
                        JarvisCyan,
                        JarvisCyanGlow,
                        Color.Transparent
                    ),
                    center = center,
                    radius = dynamicRadius * 0.32f
                ),
                radius = dynamicRadius * 0.32f,
                center = center
            )
        }
    }
}
