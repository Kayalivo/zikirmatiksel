package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeviceSkin
import kotlinx.coroutines.launch

@Composable
fun DigitalRingCounter(
    currentCount: Int,
    targetCount: Int,
    skin: DeviceSkin = DeviceSkin.EMERALD_GOLD,
    onTap: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val buttonScale = remember { Animatable(1f) }
    val buttonElevation = remember { Animatable(8f) }
    val glowAnim = remember { Animatable(0f) }

    val baseColor = Color(skin.primaryColorHex)
    val accentColor = Color(skin.accentColorHex)

    val laps = if (targetCount > 0) currentCount / targetCount else 0
    val countInLap = if (targetCount > 0) currentCount % targetCount else currentCount

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main Digital Ring Body
        Box(
            modifier = Modifier
                .size(width = 270.dp, height = 340.dp)
                .shadow(24.dp, RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp, bottomStart = 110.dp, bottomEnd = 110.dp))
                .clip(RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp, bottomStart = 110.dp, bottomEnd = 110.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            baseColor.copy(alpha = 0.95f),
                            baseColor,
                            Color(0xFF050B08)
                        ),
                        center = Offset(300f, 150f),
                        radius = 450f
                    )
                )
                .border(
                    width = 4.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            accentColor,
                            accentColor.copy(alpha = 0.4f),
                            accentColor
                        )
                    ),
                    shape = RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp, bottomStart = 110.dp, bottomEnd = 110.dp)
                )
                .padding(18.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. Digital LCD Screen Unit
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(84.dp)
                        .shadow(8.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF1E2822))
                        .border(2.5.dp, Color(0xFF3B4D42), RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // LCD Top Indicators Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TUR: $laps",
                                color = accentColor.copy(alpha = 0.9f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = if (targetCount > 0) "HEDEF: $targetCount" else "SINIRSIZ",
                                color = Color(0xFF86A391),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        // LCD Main 5-Digit Readout
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            // Ghost digits 88888 in background for realistic LCD look
                            Text(
                                text = "88888",
                                color = Color(0xFF28362D),
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                textAlign = TextAlign.End,
                                letterSpacing = 2.sp
                            )
                            // Real Digits
                            Text(
                                text = String.format("%05d", currentCount % 100000),
                                color = Color(0xFFDCFFEB),
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                textAlign = TextAlign.End,
                                letterSpacing = 2.sp
                            )
                        }
                    }
                }

                // 2. Center Brand Emblem & Small Action Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Small Reset Button
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF232D27))
                            .border(1.5.dp, accentColor.copy(alpha = 0.5f), CircleShape)
                            .clickable(onClick = onReset)
                            .testTag("digital_ring_reset_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sıfırla",
                            tint = accentColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Brand Text
                    Text(
                        text = "KAYA STUDİO",
                        color = accentColor.copy(alpha = 0.7f),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    // Target / Cycle Indicator
                    Surface(
                        shape = CircleShape,
                        color = if (targetCount > 0 && countInLap == 0 && currentCount > 0) accentColor else Color(0xFF232D27),
                        modifier = Modifier.size(16.dp)
                    ) {
                        // LED indicator for cycle completion
                    }
                }

                // 3. Big Central Tactile Press Button
                Box(
                    modifier = Modifier
                        .size(126.dp)
                        .graphicsLayer {
                            scaleX = buttonScale.value
                            scaleY = buttonScale.value
                        }
                        .shadow(
                            elevation = buttonElevation.value.dp,
                            shape = CircleShape,
                            spotColor = accentColor
                        )
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    accentColor.copy(alpha = 0.95f),
                                    accentColor,
                                    Color(0xFF6B4F09)
                                ),
                                center = Offset(60f, 60f),
                                radius = 120f
                            )
                        )
                        .border(3.5.dp, Color(0xFFFFE8A3), CircleShape)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    coroutineScope.launch {
                                        buttonScale.animateTo(0.91f, tween(50))
                                        buttonElevation.animateTo(2f, tween(50))
                                    }
                                    tryAwaitRelease()
                                    coroutineScope.launch {
                                        buttonScale.animateTo(1f, tween(100))
                                        buttonElevation.animateTo(8f, tween(100))
                                    }
                                    onTap()
                                }
                            )
                        }
                        .testTag("digital_ring_tap_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    // Inner Metallic Bevel Circle
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.25f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.3f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "BAS",
                            color = Color(0xFF2B1C00),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Ring Strap / Ergonomic Grip Texture Visual
        Box(
            modifier = Modifier
                .width(130.dp)
                .height(18.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1E2822), Color(0xFF0F1511))
                    )
                )
                .border(1.dp, Color(0xFF33443A), RoundedCornerShape(9.dp))
        )
    }
}
