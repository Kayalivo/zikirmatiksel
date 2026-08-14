package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.db.DhikrEntity
import com.example.data.model.CounterMode
import com.example.data.model.DeviceSkin
import com.example.haptics.VibrationStrength
import com.example.sound.SoundType
import com.example.ui.components.BlindTouchCounter
import com.example.ui.components.CircularProgressCounter
import com.example.ui.components.ConfirmDialog
import com.example.ui.components.DhikrVirtueDetailDialog
import com.example.ui.components.DigitalRingCounter
import com.example.ui.components.NamazTasbihatDialog
import com.example.ui.components.RealBeadTasbihCounter
import com.example.ui.components.ShareSummaryDialog
import com.example.ui.components.TargetPickerDialog
import com.example.viewmodel.AppNavScreen
import com.example.viewmodel.ZikirmatikViewModel
import kotlinx.coroutines.delay

@Composable
fun MainCounterScreen(
    viewModel: ZikirmatikViewModel,
    contentPadding: PaddingValues
) {
    val activeDhikr by viewModel.activeDhikr.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val targetAnim by viewModel.targetReachedAnimation.collectAsStateWithLifecycle()
    val todayTotal by viewModel.todayCount.collectAsStateWithLifecycle()
    val allTimeTotal by viewModel.allTimeCount.collectAsStateWithLifecycle()
    val streak by viewModel.streakCount.collectAsStateWithLifecycle()

    var showResetConfirm by remember { mutableStateOf(false) }
    var showTargetPicker by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    var showVirtueDialog by remember { mutableStateOf(false) }
    var showTasbihatDialog by remember { mutableStateOf(false) }
    var isFullscreenActive by remember { mutableStateOf(false) }
    var isAutoPacing by remember { mutableStateOf(false) }

    LaunchedEffect(targetAnim) {
        if (targetAnim) {
            delay(2600)
            viewModel.dismissTargetAnimation()
        }
    }

    // Auto Pacer Engine
    LaunchedEffect(isAutoPacing) {
        while (isAutoPacing) {
            delay(1500)
            viewModel.incrementActiveDhikr()
        }
    }

    val currentCount = activeDhikr?.currentCount ?: 0
    val targetCount = activeDhikr?.targetCount ?: 33

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Guided Namaz Tasbihat Shortcut Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { showTasbihatDialog = true }
                    .border(
                        1.5.dp,
                        Brush.linearGradient(listOf(Color(0xFFD4AF37), Color(0xFF0F5A47))),
                        RoundedCornerShape(16.dp)
                    )
                    .testTag("open_tasbihat_banner"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFD4AF37).copy(alpha = 0.2f),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color(0xFF9E7815),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Namaz Tesbihatı Asistanı",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "33 Sübhanallah • 33 Elhamdülillah • 33 Allahuekber",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF0F5A47)
                    ) {
                        Text(
                            text = "Başlat",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            // 2. Top Active Dhikr Card
            ActiveDhikrHeaderCard(
                dhikr = activeDhikr,
                onSwitchDhikr = { viewModel.navigateTo(AppNavScreen.LIBRARY) },
                onToggleFavorite = { activeDhikr?.let { viewModel.toggleFavorite(it) } },
                onTargetClick = { showTargetPicker = true },
                onVirtueClick = { showVirtueDialog = true }
            )

            // 3. Counter Mode Selector Tabs (Chips)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CounterMode.entries.forEach { mode ->
                    val isSelected = settings.counterMode == mode
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                        modifier = Modifier
                            .clickable {
                                if (mode.isVipOnly && !settings.isVipActive) {
                                    viewModel.openVipPaywall("${mode.title} VIP özelliğidir.")
                                } else {
                                    viewModel.setCounterMode(mode)
                                }
                            }
                            .testTag("mode_chip_${mode.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = when (mode) {
                                    CounterMode.DIGITAL_RING -> Icons.Default.TouchApp
                                    CounterMode.REAL_BEADS -> Icons.Default.Grain
                                    CounterMode.MODERN_HALO -> Icons.Default.RadioButtonChecked
                                    CounterMode.BLIND_TOUCH -> Icons.Default.VisibilityOff
                                },
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = mode.title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (mode.isVipOnly) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = "VIP",
                                    tint = Color(0xFFD4AF37),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 4. Skin Selection Row (When in Digital Ring or Real Beads mode)
            if (settings.counterMode == CounterMode.DIGITAL_RING || settings.counterMode == CounterMode.REAL_BEADS) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kaplama:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                    DeviceSkin.entries.forEach { skin ->
                        val isSelected = settings.deviceSkin == skin
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(skin.primaryColorHex),
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) Color(skin.accentColorHex) else Color.Gray.copy(alpha = 0.4f)
                            ),
                            modifier = Modifier
                                .clickable {
                                    if (skin.isVipOnly && !settings.isVipActive) {
                                        viewModel.openVipPaywall("${skin.title} kaplaması VIP ayrıcalığıdır.")
                                    } else {
                                        viewModel.setDeviceSkin(skin)
                                    }
                                }
                                .testTag("skin_chip_${skin.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(skin.accentColorHex),
                                    modifier = Modifier.size(8.dp)
                                ) {}
                                Text(
                                    text = skin.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = Color.White
                                )
                                if (skin.isVipOnly && !settings.isVipActive) {
                                    Icon(
                                        imageVector = Icons.Default.WorkspacePremium,
                                        contentDescription = "VIP",
                                        tint = Color(0xFFD4AF37),
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5. Active Interactive Counter View (Switching according to mode)
            Crossfade(
                targetState = settings.counterMode,
                label = "counter_mode_crossfade"
            ) { mode ->
                when (mode) {
                    CounterMode.DIGITAL_RING -> DigitalRingCounter(
                        currentCount = currentCount,
                        targetCount = targetCount,
                        skin = settings.deviceSkin,
                        onTap = { viewModel.incrementActiveDhikr() },
                        onReset = { showResetConfirm = true }
                    )
                    CounterMode.REAL_BEADS -> RealBeadTasbihCounter(
                        currentCount = currentCount,
                        targetCount = targetCount,
                        skin = settings.deviceSkin,
                        onPullBead = { viewModel.incrementActiveDhikr() }
                    )
                    CounterMode.MODERN_HALO -> CircularProgressCounter(
                        currentCount = currentCount,
                        targetCount = targetCount,
                        onTap = { viewModel.incrementActiveDhikr() }
                    )
                    CounterMode.BLIND_TOUCH -> BlindTouchCounter(
                        currentCount = currentCount,
                        targetCount = targetCount,
                        dhikrName = activeDhikr?.title ?: "Zikir",
                        onTap = { viewModel.incrementActiveDhikr() },
                        onExitBlindMode = { viewModel.setCounterMode(CounterMode.DIGITAL_RING) }
                    )
                }
            }

            // 6. Action Control Bar (Quick Toggles, Undo, Auto-Pace, Share)
            CounterActionBar(
                soundType = settings.soundType,
                vibrationStrength = settings.vibrationStrength,
                isAutoPacing = isAutoPacing,
                onToggleAutoPacing = { isAutoPacing = !isAutoPacing },
                onToggleSound = {
                    val availableSounds = if (settings.isVipActive) {
                        listOf(SoundType.CLICK, SoundType.WATER, SoundType.BELL, SoundType.KUKA, SoundType.NEY, SoundType.OFF)
                    } else {
                        listOf(SoundType.CLICK, SoundType.WATER, SoundType.BELL, SoundType.OFF)
                    }
                    val currentIndex = availableSounds.indexOf(settings.soundType)
                    val nextSound = availableSounds[(currentIndex + 1).coerceAtLeast(0) % availableSounds.size]
                    viewModel.setSoundType(nextSound)
                },
                onToggleVibration = {
                    val nextVib = when (settings.vibrationStrength) {
                        VibrationStrength.LIGHT -> VibrationStrength.MEDIUM
                        VibrationStrength.MEDIUM -> VibrationStrength.STRONG
                        VibrationStrength.STRONG -> VibrationStrength.OFF
                        VibrationStrength.OFF -> VibrationStrength.LIGHT
                    }
                    viewModel.setVibrationStrength(nextVib)
                },
                onStepBack = { viewModel.decrementActiveDhikr() },
                onReset = { showResetConfirm = true },
                onFullscreen = { isFullscreenActive = true },
                onShare = { showShareDialog = true },
                isUndoEnabled = currentCount > 0
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Target Reached Celebration Toast / Banner
        AnimatedVisibility(
            visible = targetAnim,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                shadowElevation = 10.dp,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "✨", fontSize = 20.sp)
                    Column {
                        Text(
                            text = "Hedef Tamamlandı!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "${activeDhikr?.title ?: "Zikir"} tesbihatı tamamlandı.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Fullscreen Mode Tap Anywhere Overlay
        if (isFullscreenActive) {
            FullscreenTapOverlay(
                activeDhikr = activeDhikr,
                currentCount = currentCount,
                targetCount = targetCount,
                onTap = { viewModel.incrementActiveDhikr() },
                onClose = { isFullscreenActive = false }
            )
        }
    }

    // Dialogs
    if (showResetConfirm) {
        ConfirmDialog(
            title = "Sayacı Sıfırla",
            message = "${activeDhikr?.title ?: "Bu zikir"} sayacı sıfırlansın mı? (Genel toplam istatistikleriniz korunur).",
            confirmText = "Sıfırla",
            dismissText = "Vazgeç",
            isDestructive = true,
            onConfirm = { viewModel.resetActiveDhikr() },
            onDismiss = { showResetConfirm = false }
        )
    }

    if (showTargetPicker) {
        TargetPickerDialog(
            currentTarget = targetCount,
            onDismiss = { showTargetPicker = false },
            onTargetSelected = { newTarget ->
                viewModel.updateActiveDhikrTarget(newTarget)
            }
        )
    }

    if (showShareDialog) {
        ShareSummaryDialog(
            dhikrTitle = activeDhikr?.title,
            dhikrCount = currentCount,
            todayTotal = todayTotal,
            allTimeTotal = allTimeTotal,
            streak = streak,
            onDismiss = { showShareDialog = false }
        )
    }

    if (showVirtueDialog && activeDhikr != null) {
        DhikrVirtueDetailDialog(
            dhikr = activeDhikr!!,
            onDismiss = { showVirtueDialog = false }
        )
    }

    if (showTasbihatDialog) {
        NamazTasbihatDialog(
            onDismiss = { showTasbihatDialog = false },
            onStepIncrement = {
                viewModel.incrementActiveDhikr()
            },
            onCompletedDhikr = { totalDone ->
                // Refresh and reward
            }
        )
    }
}

@Composable
private fun ActiveDhikrHeaderCard(
    dhikr: DhikrEntity?,
    onSwitchDhikr: () -> Unit,
    onToggleFavorite: () -> Unit,
    onTargetClick: () -> Unit,
    onVirtueClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Switch Dhikr Button
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .clickable(onClick = onSwitchDhikr)
                        .testTag("switch_dhikr_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatListBulleted,
                            contentDescription = "Zikir Değiştir",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Zikir Değiştir",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Virtue / Info Button
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clickable(onClick = onVirtueClick)
                            .testTag("virtue_info_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = "Fazilet",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Fazileti",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Target Pill
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier
                            .clickable(onClick = onTargetClick)
                            .testTag("target_pill_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrackChanges,
                                contentDescription = "Hedef",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if ((dhikr?.targetCount ?: 0) > 0) "${dhikr?.targetCount}" else "Serbest",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Favorite Toggle
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("favorite_dhikr_button")
                    ) {
                        Icon(
                            imageVector = if (dhikr?.isFavorite == true) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Favori",
                            tint = if (dhikr?.isFavorite == true) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Arabic Text Display (if available)
            if (!dhikr?.arabicText.isNullOrBlank()) {
                Text(
                    text = dhikr?.arabicText ?: "",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            // Title
            Text(
                text = dhikr?.title ?: "Zikir Seçiniz",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            // Meaning / Transliteration
            if (!dhikr?.meaning.isNullOrBlank()) {
                Text(
                    text = dhikr?.meaning ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun CounterActionBar(
    soundType: SoundType,
    vibrationStrength: VibrationStrength,
    isAutoPacing: Boolean,
    onToggleAutoPacing: () -> Unit,
    onToggleSound: () -> Unit,
    onToggleVibration: () -> Unit,
    onStepBack: () -> Unit,
    onReset: () -> Unit,
    onFullscreen: () -> Unit,
    onShare: () -> Unit,
    isUndoEnabled: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Auto Pacer (Play/Pause)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FilledTonalIconButton(
                    onClick = onToggleAutoPacing,
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = if (isAutoPacing) Color(0xFF2E7D32) else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.testTag("action_autopace_button")
                ) {
                    Icon(
                        imageVector = if (isAutoPacing) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Otomatik Zikir",
                        tint = if (isAutoPacing) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = if (isAutoPacing) "Durdur" else "Otomatik",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp
                )
            }

            // 2. Step Back (-1) Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FilledTonalIconButton(
                    onClick = onStepBack,
                    enabled = isUndoEnabled,
                    modifier = Modifier.testTag("action_undo_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Undo,
                        contentDescription = "Geri Al (-1)"
                    )
                }
                Text("Geri Al", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
            }

            // 3. Sound Toggle
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FilledTonalIconButton(
                    onClick = onToggleSound,
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = if (soundType != SoundType.OFF) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.testTag("action_sound_button")
                ) {
                    Icon(
                        imageVector = if (soundType != SoundType.OFF) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                        contentDescription = "Ses: ${soundType.title}"
                    )
                }
                Text(
                    text = if (soundType == SoundType.OFF) "Sessiz" else "Ses",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp
                )
            }

            // 4. Vibration Toggle
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FilledTonalIconButton(
                    onClick = onToggleVibration,
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = if (vibrationStrength != VibrationStrength.OFF) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.testTag("action_vibrate_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Vibration,
                        contentDescription = "Titreşim: ${vibrationStrength.title}",
                        tint = if (vibrationStrength != VibrationStrength.OFF) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                }
                Text(
                    text = if (vibrationStrength == VibrationStrength.OFF) "Tit. Kapalı" else "Titreşim",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp
                )
            }

            // 5. Fullscreen Mode
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FilledTonalIconButton(
                    onClick = onFullscreen,
                    modifier = Modifier.testTag("action_fullscreen_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Fullscreen,
                        contentDescription = "Tam Ekran Zikir"
                    )
                }
                Text("Tam Ekran", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
            }

            // 6. Share Summary
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FilledTonalIconButton(
                    onClick = onShare,
                    modifier = Modifier.testTag("action_share_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Paylaş"
                    )
                }
                Text("Paylaş", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun FullscreenTapOverlay(
    activeDhikr: DhikrEntity?,
    currentCount: Int,
    targetCount: Int,
    onTap: () -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onTap() })
            }
            .testTag("fullscreen_tap_area")
    ) {
        // Exit Button at Top Right
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                .testTag("fullscreen_exit_button")
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Tam Ekrandan Çık",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Center Big Display
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = activeDhikr?.title ?: "Zikir",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "$currentCount",
                fontSize = 96.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = (-1).sp
            )

            if (targetCount > 0) {
                val rem = maxOf(0, targetCount - (currentCount % targetCount))
                Text(
                    text = "Kalan: $rem / Hedef: $targetCount",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Saymak için ekranda herhangi bir yere dokunun 👆",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }
    }
}
