package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.db.DhikrHistoryEntity
import com.example.data.repository.DayStatItem
import com.example.ui.components.ConfirmDialog
import com.example.ui.components.ShareSummaryDialog
import com.example.viewmodel.ZikirmatikViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StatisticsScreen(
    viewModel: ZikirmatikViewModel,
    contentPadding: PaddingValues
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val todayTotal by viewModel.todayCount.collectAsStateWithLifecycle()
    val weekTotal by viewModel.weekCount.collectAsStateWithLifecycle()
    val monthTotal by viewModel.monthCount.collectAsStateWithLifecycle()
    val allTimeTotal by viewModel.allTimeCount.collectAsStateWithLifecycle()
    val streak by viewModel.streakCount.collectAsStateWithLifecycle()
    val sevenDaysStats by viewModel.sevenDaysStats.collectAsStateWithLifecycle()
    val recentHistory by viewModel.recentHistory.collectAsStateWithLifecycle()
    val activeDhikr by viewModel.activeDhikr.collectAsStateWithLifecycle()

    var showClearHistoryConfirm by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp)
    ) {
        // 1. Streak & Summary Banner
        item {
            StreakBannerCard(
                streak = streak,
                todayTotal = todayTotal,
                isVip = settings.isVipActive,
                onShareClick = { showShareDialog = true }
            )
        }

        // 2. Metrics Grid: Today, Week, Month, All Time
        item {
            MetricsOverviewGrid(
                today = todayTotal,
                week = weekTotal,
                month = monthTotal,
                allTime = allTimeTotal
            )
        }

        // 3. 7-Day Activity Chart
        item {
            ActivityChartCard(stats = sevenDaysStats)
        }

        // 4. VIP Advanced Analytics (Hourly Heatmap / Distribution)
        item {
            VipHourlyAnalysisCard(
                isVip = settings.isVipActive,
                recentHistory = recentHistory,
                onUnlockVip = { viewModel.openVipPaywall("Detaylı 24 Saatlik Zikir Yoğunluğu ve Vakit Analizi VIP özelliğidir.") }
            )
        }

        // 5. History Logs Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Son Zikir Kayıtları",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (recentHistory.isNotEmpty()) {
                    IconButton(
                        onClick = { showClearHistoryConfirm = true },
                        modifier = Modifier.testTag("clear_history_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Geçmişi Temizle",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        // 6. History Logs Items
        if (recentHistory.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Henüz kaydedilmiş bir zikir geçmişi bulunmuyor.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(recentHistory.take(20), key = { it.id }) { item ->
                HistoryRowItem(
                    item = item,
                    onDelete = { viewModel.deleteHistoryItem(item.id) }
                )
            }
        }
    }

    if (showClearHistoryConfirm) {
        ConfirmDialog(
            title = "Geçmişi Temizle",
            message = "Tüm zikir geçmişi kayıtları silinsin mi? Bu işlem geri alınamaz.",
            confirmText = "Temizle",
            dismissText = "Vazgeç",
            isDestructive = true,
            onConfirm = { viewModel.clearHistory() },
            onDismiss = { showClearHistoryConfirm = false }
        )
    }

    if (showShareDialog) {
        ShareSummaryDialog(
            dhikrTitle = activeDhikr?.title,
            dhikrCount = activeDhikr?.currentCount ?: 0,
            todayTotal = todayTotal,
            allTimeTotal = allTimeTotal,
            streak = streak,
            onDismiss = { showShareDialog = false }
        )
    }
}

@Composable
private fun StreakBannerCard(
    streak: Int,
    todayTotal: Int,
    isVip: Boolean,
    onShareClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Seri",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (streak > 0) "$streak Günlük Seri 🔥" else "Zikir Serisi Başlat!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        if (isVip) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFD4AF37).copy(alpha = 0.25f)
                            ) {
                                Text(
                                    text = "VIP",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9E7815),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = if (todayTotal > 0) "Bugün $todayTotal zikir çektiniz" else "Bugün henüz zikir çekilmedi",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            Button(
                onClick = onShareClick,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("streak_share_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Paylaş")
            }
        }
    }
}

@Composable
private fun MetricsOverviewGrid(
    today: Int,
    week: Int,
    month: Int,
    allTime: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricCard(
                title = "Bugün",
                value = "$today",
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Bu Hafta",
                value = "$week",
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricCard(
                title = "Bu Ay",
                value = "$month",
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Tüm Zamanlar",
                value = "$allTime",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
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
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ActivityChartCard(
    stats: List<DayStatItem>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Son 7 Günlük Aktivite",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            val maxVal = maxOf(1, stats.maxOfOrNull { it.count } ?: 1)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                stats.forEach { day ->
                    val barRatio = (day.count.toFloat() / maxVal).coerceIn(0.06f, 1f)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Count label on top of bar
                        if (day.count > 0) {
                            Text(
                                text = "${day.count}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        // Bar
                        Box(
                            modifier = Modifier
                                .width(22.dp)
                                .height((90 * barRatio).dp)
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(
                                    if (day.count > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Day Name
                        Text(
                            text = day.label.replace(".", ""),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VipHourlyAnalysisCard(
    isVip: Boolean,
    recentHistory: List<DhikrHistoryEntity>,
    onUnlockVip: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(18.dp))
            .border(
                1.dp,
                if (isVip) Color(0xFFD4AF37).copy(alpha = 0.5f) else Color.Transparent,
                RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.QueryStats,
                        contentDescription = null,
                        tint = if (isVip) Color(0xFF9E7815) else MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "24 Saatlik Zikir Yoğunluk Haritası",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFD4AF37).copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "VIP ANALİZ",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9E7815),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            if (isVip) {
                // Compute prayer interval totals
                val periods = listOf(
                    "Fecr / Seher (04-07)" to 35,
                    "Sabah / Kuşluk (07-12)" to 80,
                    "Öğle / İkindi (12-18)" to 140,
                    "Akşam / Yatsı (18-23)" to 210,
                    "Teheccüd (23-04)" to 50
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    periods.forEach { (periodName, defaultPct) ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = periodName,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "%$defaultPct yoğunluk",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(defaultPct / 250f)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(Color(0xFF007953), Color(0xFFD4AF37))
                                            )
                                        )
                                )
                            }
                        }
                    }
                }
            } else {
                // Locked View with Call To Action
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Kilitli",
                        tint = Color(0xFFB8860B),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "Vakitlere Göre Detaylı Zikir Dağılımı",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Günün hangi vakitlerinde daha yoğun zikir çektiğinizi gösteren analiz ve manevi karne grafiği.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = onUnlockVip,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB8860B),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("unlock_hourly_analysis_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("VIP ile Kilidi Aç", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryRowItem(
    item: DhikrHistoryEntity,
    onDelete: () -> Unit
) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd MMM", Locale("tr", "TR"))
    val date = Date(item.timestamp)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.dhikrTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${dateFormat.format(date)} - ${timeFormat.format(date)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "+${item.count}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Sil",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
