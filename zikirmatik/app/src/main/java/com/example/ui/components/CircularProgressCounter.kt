package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularProgressCounter(
    currentCount: Int,
    targetCount: Int,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val scaleAnim = remember { Animatable(1f) }
    val glowAlpha = remember { Animatable(0f) }

    val progress = if (targetCount > 0) {
        val currentInCycle = currentCount % targetCount
        if (currentInCycle == 0 && currentCount > 0) 1f else (currentInCycle.toFloat() / targetCount)
    } else {
        1f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
        label = "progress"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    val laps = if (targetCount > 0) currentCount / targetCount else 0

    Box(
        modifier = modifier
            .size(280.dp)
            .graphicsLayer {
                scaleX = scaleAnim.value
                scaleY = scaleAnim.value
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        coroutineScope.launch {
                            scaleAnim.animateTo(0.93f, tween(60))
                            glowAlpha.animateTo(0.35f, tween(60))
                        }
                        tryAwaitRelease()
                        coroutineScope.launch {
                            scaleAnim.animateTo(1f, tween(120))
                            glowAlpha.animateTo(0f, tween(180))
                        }
                        onTap()
                    }
                )
            }
            .testTag("counter_tap_button"),
        contentAlignment = Alignment.Center
    ) {
        // Outer Glowing Radial Atmosphere
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    if (glowAlpha.value > 0f) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    primaryColor.copy(alpha = glowAlpha.value),
                                    Color.Transparent
                                ),
                                center = center,
                                radius = size.minDimension / 1.8f
                            )
                        )
                    }
                }
        )

        // Progress Arc Canvas with Tasbih Beads
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            val strokeWidth = 10.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val arcTopLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            val arcSize = Size(diameter, diameter)

            // Background Ring Track
            drawArc(
                color = surfaceVariant.copy(alpha = 0.6f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Dynamic Gradient Progress Arc
            if (animatedProgress > 0f) {
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            primaryColor,
                            secondaryColor,
                            primaryColor
                        ),
                        center = center
                    ),
                    startAngle = -90f,
                    sweepAngle = animatedProgress * 360f,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth + 2.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // Decorative Tasbih Bead Markers (every 30 degrees = 12 beads around ring)
            val beadRadius = diameter / 2
            val centerOffset = center
            for (i in 0 until 12) {
                val angleDeg = i * 30.0 - 90.0
                val angleRad = Math.toRadians(angleDeg)
                val beadX = centerOffset.x + (beadRadius * cos(angleRad)).toFloat()
                val beadY = centerOffset.y + (beadRadius * sin(angleRad)).toFloat()

                val beadPassed = (i.toFloat() / 12f) <= animatedProgress
                drawCircle(
                    color = if (beadPassed) secondaryColor else surfaceVariant,
                    radius = if (i % 3 == 0) 3.5.dp.toPx() else 2.dp.toPx(),
                    center = Offset(beadX, beadY)
                )
            }
        }

        // Inner Tactile Button Surface
        Box(
            modifier = Modifier
                .size(216.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    ambientColor = primaryColor.copy(alpha = 0.3f),
                    spotColor = primaryColor.copy(alpha = 0.5f)
                )
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            surfaceColor,
                            surfaceVariant.copy(alpha = 0.95f)
                        ),
                        center = Offset.Zero
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                // Lap / Cycle Tag
                if (targetCount > 0 && laps > 0) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = primaryColor.copy(alpha = 0.15f),
                                shape = CircleShape
                            )
                            .padding(horizontal = 10.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "$laps. Tur",
                            style = MaterialTheme.typography.labelSmall,
                            color = primaryColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                }

                // Main Digits Display
                Text(
                    text = "$currentCount",
                    fontSize = if (currentCount > 9999) 40.sp else if (currentCount > 999) 48.sp else 58.sp,
                    fontWeight = FontWeight.Bold,
                    color = onSurface,
                    letterSpacing = (-0.5).sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("counter_value_text")
                )

                // Target Subtitle
                if (targetCount > 0) {
                    val remaining = maxOf(0, targetCount - (currentCount % targetCount))
                    val isTargetHit = (currentCount % targetCount == 0 && currentCount > 0)
                    Text(
                        text = if (isTargetHit) "Hedef Tamamlandı! ✨" else "Kalan: $remaining / $targetCount",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isTargetHit) secondaryColor else onSurface.copy(alpha = 0.6f),
                        fontWeight = if (isTargetHit) FontWeight.Bold else FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = "Serbest Zikir",
                        style = MaterialTheme.typography.bodySmall,
                        color = onSurface.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
