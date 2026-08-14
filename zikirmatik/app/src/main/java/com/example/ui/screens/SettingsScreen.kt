package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.StayCurrentPortrait
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.haptics.VibrationStrength
import com.example.sound.SoundType
import com.example.viewmodel.ZikirmatikViewModel

@Composable
fun SettingsScreen(
    viewModel: ZikirmatikViewModel,
    contentPadding: PaddingValues
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 48.dp)
    ) {
        // 1. VIP Membership Banner
        item {
            var promoInput by remember { mutableStateOf("") }
            var promoMessage by remember { mutableStateOf<String?>(null) }
            var promoIsSuccess by remember { mutableStateOf(false) }
            var showPromoBoxInVip by remember { mutableStateOf(false) }

            if (settings.isVipActive) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(22.dp))
                        .border(
                            1.5.dp,
                            Brush.linearGradient(
                                listOf(Color(0xFFE5C058), Color(0xFF9E7815), Color(0xFFECC244))
                            ),
                            RoundedCornerShape(22.dp)
                        )
                        .testTag("vip_active_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.radialGradient(
                                                listOf(Color(0xFFFFDF7D), Color(0xFFC79A1E))
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }

                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = if (settings.isLifetimeVip) "VIP Üyelik (Ömür Boyu)" else "VIP Deneme Süresi",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF9E7815)
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color(0xFFD4AF37).copy(alpha = 0.2f)
                                        ) {
                                            Text(
                                                text = if (settings.isLifetimeVip) "LIFETIME" else "KAMPANYA",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF9E7815),
                                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = if (settings.vipPlanName.isNotEmpty()) settings.vipPlanName else "Tüm VIP Özellikler Açık",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (settings.isTimedVip) {
                                        Text(
                                            text = settings.vipStatusBadgeText,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF2E7D32),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            OutlinedButton(
                                onClick = { viewModel.openVipPaywall("VIP Ayrıcalıklarınız") },
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("view_vip_benefits_button")
                            ) {
                                Text("Ayrıcalıklar", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        // Code upgrade trigger for users on timed trial wanting to enter another code
                        if (!showPromoBoxInVip) {
                            TextButton(
                                onClick = { showPromoBoxInVip = true },
                                modifier = Modifier.align(Alignment.End),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Key,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "Yeni Kod Gir / Süre Uzat",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = promoInput,
                                            onValueChange = { 
                                                promoInput = it
                                                promoMessage = null
                                            },
                                            placeholder = { Text("Yeni promosyon kodu", fontSize = 11.sp) },
                                            singleLine = true,
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(44.dp)
                                        )

                                        Button(
                                            onClick = {
                                                if (promoInput.isNotBlank()) {
                                                    val res = viewModel.redeemPromoCode(promoInput)
                                                    promoIsSuccess = res.isSuccess
                                                    promoMessage = res.message
                                                    if (res.isSuccess) promoInput = ""
                                                }
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                            modifier = Modifier.height(44.dp)
                                        ) {
                                            Text("Uygula", fontSize = 11.sp)
                                        }
                                    }

                                    if (promoMessage != null) {
                                        Text(
                                            text = promoMessage!!,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (promoIsSuccess) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(22.dp))
                        .border(
                            1.5.dp,
                            Brush.linearGradient(
                                listOf(Color(0xFFECC244), Color(0xFFB8860B), Color(0xFFFFDF7D))
                            ),
                            RoundedCornerShape(22.dp)
                        )
                        .testTag("vip_upgrade_banner"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFFFFDF7D), Color(0xFFC79A1E))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Zikirmatik VIP'ye Geçin",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9E7815)
                                )
                                Text(
                                    text = "Lüks Temalar, Kuka & Ney Sesleri, Sınırsız Zikir",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.openVipPaywall("Tüm VIP özelliklere erişin") },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFB8860B),
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("upgrade_vip_settings_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CardGiftcard,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("VIP Planları İncele", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        // Promo Code Input Box for Close Circle & Developer & Weekly Campaigns
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Key,
                                        contentDescription = null,
                                        tint = Color(0xFFB8860B),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "🎁 Promosyon / Kampanya / Hediye Kodu",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Text(
                                    text = "Sosyal medya veya haftalık reklam kampanyası kodunuzu girerek 5 günlük ücretsiz VIP deneme süresi kazanabilirsiniz.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = promoInput,
                                        onValueChange = { 
                                            promoInput = it
                                            promoMessage = null
                                        },
                                        placeholder = { Text("Kodu buraya yazın...", fontSize = 12.sp) },
                                        singleLine = true,
                                        shape = RoundedCornerShape(10.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFFB8860B),
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .testTag("promo_code_input")
                                    )

                                    Button(
                                        onClick = {
                                            if (promoInput.isNotBlank()) {
                                                val res = viewModel.redeemPromoCode(promoInput)
                                                promoIsSuccess = res.isSuccess
                                                promoMessage = res.message
                                                if (res.isSuccess) {
                                                    promoInput = ""
                                                }
                                            }
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF2E7D32),
                                            contentColor = Color.White
                                        ),
                                        modifier = Modifier
                                            .height(48.dp)
                                            .testTag("apply_promo_code_button")
                                    ) {
                                        Text("Kullan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                if (promoMessage != null) {
                                    Text(
                                        text = promoMessage!!,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (promoIsSuccess) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Vibration & Haptics Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Vibration,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Titreşim & Dokunsal Geri Bildirim",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Sayaç basışlarında ve hedef tamamlandığında hissedilen titreşim şiddeti:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        VibrationStrength.entries.forEach { strength ->
                            val isSelected = settings.vibrationStrength == strength
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setVibrationStrength(strength) },
                                label = { Text(strength.title.replace(" Titreşim", "")) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Target Vibration Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Hedefe Ulaşınca Titret",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Belirlenen hedef tamamlandığında özel ritmik titreşim verir.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = settings.vibrateOnTarget,
                            onCheckedChange = { viewModel.setVibrateOnTarget(it) },
                            modifier = Modifier.testTag("vibrate_on_target_switch")
                        )
                    }
                }
            }
        }

        // 3. Sound Effects Section (with VIP badges)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Ses Efektleri & Paketleri",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Sayaç basışlarında çalınacak otantik ses tonunu belirleyin:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SoundType.entries.chunked(2).forEach { rowSounds ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowSounds.forEach { sound ->
                                    val isSelected = settings.soundType == sound
                                    val isLocked = sound.isVip && !settings.isVipActive

                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            if (isLocked) {
                                                viewModel.openVipPaywall("${sound.title} ses paketi VIP özelliğidir.")
                                            } else {
                                                viewModel.setSoundType(sound)
                                            }
                                        },
                                        label = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(sound.title)
                                                if (sound.isVip) {
                                                    if (isLocked) {
                                                        Icon(
                                                            imageVector = Icons.Default.Lock,
                                                            contentDescription = "VIP",
                                                            modifier = Modifier.size(12.dp),
                                                            tint = Color(0xFFB8860B)
                                                        )
                                                    } else {
                                                        Text(
                                                            text = "VIP",
                                                            fontSize = 9.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = Color(0xFFB8860B)
                                                        )
                                                    }
                                                }
                                            }
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("sound_chip_${sound.id}")
                                    )
                                }
                            }
                        }
                    }

                    // Target Sound Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Hedefe Ulaşınca Tını Çal",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Hedefe ulaşıldığında ferahlatıcı makam tınısı çalar.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = settings.soundOnTarget,
                            onCheckedChange = { viewModel.setSoundOnTarget(it) },
                            modifier = Modifier.testTag("sound_on_target_switch")
                        )
                    }
                }
            }
        }

        // 4. Smart Dhikr Reminders Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Manevi Hatırlatıcılar & Virdler",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Friday Salawat Reminder
                    ReminderRowItem(
                        title = "Cuma Kehf & Salavat Hatırlatıcısı",
                        subtitle = "Her Cuma günü saat 11:00'de salavat ve Kehf suresi virdi hatırlatır.",
                        isVip = true,
                        isUserVip = settings.isVipActive,
                        checked = settings.reminderFriday,
                        onCheckedChange = { viewModel.setReminderFriday(it) },
                        testTag = "friday_reminder_switch"
                    )

                    // Morning & Evening Dhikr Reminder
                    ReminderRowItem(
                        title = "Sabah & Akşam Virdi Hatırlatıcısı",
                        subtitle = "Günde 2 vakit (07:00 ve 19:00) günlük zikirlerinizi hatırlatır.",
                        isVip = true,
                        isUserVip = settings.isVipActive,
                        checked = settings.reminderMorningEvening,
                        onCheckedChange = { viewModel.setReminderMorningEvening(it) },
                        testTag = "morning_evening_reminder_switch"
                    )

                    // Tahajjud Reminder
                    ReminderRowItem(
                        title = "Gece Teheccüd & İstiğfar Hatırlatıcısı",
                        subtitle = "Gece 03:30'da istiğfar ve hacet virdi için manevi uyarı verir.",
                        isVip = true,
                        isUserVip = settings.isVipActive,
                        checked = settings.reminderTahajjud,
                        onCheckedChange = { viewModel.setReminderTahajjud(it) },
                        testTag = "tahajjud_reminder_switch"
                    )
                }
            }
        }

        // 5. Screen & Convenience
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.StayCurrentPortrait,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Ekran & Kullanım Kolaylığı",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Keep Screen On
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ekranı Sürekli Açık Tut",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Zikir çekerken telefonun ekranının kapanmasını engeller.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = settings.keepScreenOn,
                            onCheckedChange = { viewModel.setKeepScreenOn(it) },
                            modifier = Modifier.testTag("keep_screen_on_switch")
                        )
                    }
                }
            }
        }

        // 6. Developer & About Section (Requested specifically by user)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(22.dp))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        RoundedCornerShape(22.dp)
                    )
                    .testTag("developer_about_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color(0xFFE91E63),
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "Geliştirici & Manevi İthaf",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Special Message Text Requested by User
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Bu uygulama Kaya Studio tarafından baştan sona hazırlanmış olup tüm yasal sorumlulukları tarafınca saklıdır. Geliştirilme sürecinde baştan sona yardımcı olan sevgilim Beyza Nur'a sonsuz teşekkürlerimi buradan da iletmeyi borç bilirim.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(14.dp)
                        )
                    }

                    // Instagram link button
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFE1306C).copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE1306C).copy(alpha = 0.4f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/kayalivo"))
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }
                            .testTag("instagram_kayalivo_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "📸",
                                    fontSize = 18.sp
                                )
                                Column {
                                    Text(
                                        text = "İnstagram: @kayalivo",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFC13584)
                                    )
                                    Text(
                                        text = "Kaya Studio & Geliştirici İletişim",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = "Profili Aç",
                                tint = Color(0xFFC13584),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "«Ey iman edenler! Allah'ı çokça zikredin ve O'nu sabah akşam tesbih edin.» (Ahzab Suresi, 41-42)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )

                    Text(
                        text = "Kaya Studio Since 2026 v1.02",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
private fun ReminderRowItem(
    title: String,
    subtitle: String,
    isVip: Boolean,
    isUserVip: Boolean,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (isVip) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFD4AF37).copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "VIP",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB8860B),
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(testTag)
        )
    }
}
