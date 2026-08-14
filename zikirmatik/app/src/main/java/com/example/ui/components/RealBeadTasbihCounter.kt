package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeviceSkin
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun RealBeadTasbihCounter(
    currentCount: Int,
    targetCount: Int,
    skin: DeviceSkin = DeviceSkin.EMERALD_GOLD,
    onPullBead: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var dragAccumulator by remember { mutableFloatStateOf(0f) }
    val beadOffsetAnim = remember { Animatable(0f) }

    val beadPrimaryColor = Color(skin.primaryColorHex)
    val beadAccentColor = Color(skin.accentColorHex)

    val dragThreshold = 70f // Distance in pixels to trigger next bead

    val draggableState = rememberDraggableState { delta ->
        dragAccumulator += delta
        coroutineScope.launch {
            beadOffsetAnim.snapTo(dragAccumulator % dragThreshold)
        }
        if (abs(dragAccumulator) >= dragThreshold) {
            dragAccumulator = 0f
            coroutineScope.launch {
                beadOffsetAnim.animateTo(
                    0f,
                    spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
                )
            }
            onPullBead()
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .shadow(16.dp, RoundedCornerShape(28.dp))
            .testTag("real_bead_counter_card"),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bead Header: Count & Target
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hakiki Tesbih Modu",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Taneleri aşağı/yukarı kaydırın",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "$currentCount ${if (targetCount > 0) "/ $targetCount" else ""}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Central Bead Interactive Canvas Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .draggable(
                        state = draggableState,
                        orientation = Orientation.Vertical,
                        onDragStopped = {
                            if (abs(dragAccumulator) > 20f) {
                                onPullBead()
                            }
                            dragAccumulator = 0f
                            coroutineScope.launch {
                                beadOffsetAnim.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                            }
                        }
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val centerX = size.width / 2f
                    val centerY = size.height / 2f

                    // Draw Silk Thread Line
                    drawLine(
                        color = Color(0xFFD4AF37).copy(alpha = 0.7f),
                        start = Offset(centerX, 0f),
                        end = Offset(centerX, size.height),
                        strokeWidth = 4.dp.toPx()
                    )

                    // Draw Imame / Top Piece Decoration
                    val imamePath = Path().apply {
                        moveTo(centerX, 15.dp.toPx())
                        lineTo(centerX - 14.dp.toPx(), 45.dp.toPx())
                        lineTo(centerX + 14.dp.toPx(), 45.dp.toPx())
                        close()
                    }
                    drawPath(
                        path = imamePath,
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFFFFDF7D), Color(0xFF9E7815))
                        )
                    )

                    // Draw 7 Visible Beads along the central axis with physics offset
                    val beadRadius = 26.dp.toPx()
                    val spacing = 52.dp.toPx()
                    val currentAnimY = beadOffsetAnim.value

                    for (i in -3..3) {
                        val beadY = centerY + (i * spacing) + currentAnimY
                        if (beadY in -beadRadius..(size.height + beadRadius)) {
                            // 3D Sphere Radial Gradient for authentic luster
                            val isCenterBead = i == 0
                            val radius = if (isCenterBead) beadRadius * 1.15f else beadRadius

                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.6f),
                                        beadAccentColor,
                                        beadPrimaryColor,
                                        Color(0xFF0D1812)
                                    ),
                                    center = Offset(centerX - radius * 0.35f, beadY - radius * 0.35f),
                                    radius = radius * 1.3f
                                ),
                                radius = radius,
                                center = Offset(centerX, beadY)
                            )

                            // Subtle Golden Ring between beads
                            drawCircle(
                                color = Color(0xFFFFDF7D).copy(alpha = 0.8f),
                                radius = radius,
                                center = Offset(centerX, beadY),
                                style = Stroke(width = 1.5.dp.toPx())
                            )
                        }
                    }
                }

                // Interactive Tap Fallback Button Overlay in bottom-right
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                    shadowElevation = 6.dp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .size(48.dp)
                        .clip(CircleShape)
                        .shadow(4.dp, CircleShape)
                        .testTag("real_bead_tap_btn"),
                    onClick = onPullBead
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.TouchApp,
                            contentDescription = "Tıkla",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Bottom Progress Info
            Text(
                text = "Dokunarak veya yukarı-aşağı çekerek tesbihi çekebilirsiniz.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
        }
    }
}
