package com.example

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.VipPaywallDialog
import com.example.ui.screens.DhikrLibraryScreen
import com.example.ui.screens.MainCounterScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StatisticsScreen
import com.example.ui.screens.ThemesScreen
import com.example.ui.theme.ZikirmatikTheme
import com.example.viewmodel.AppNavScreen
import com.example.viewmodel.ZikirmatikViewModel
import com.example.viewmodel.ZikirmatikViewModelFactory

data class NavTabItem(
    val screen: AppNavScreen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {

    private val viewModel: ZikirmatikViewModel by viewModels {
        val app = application as ZikirmatikApp
        ZikirmatikViewModelFactory(
            app.dhikrRepository,
            app.settingsRepository,
            app.soundPlayer,
            app.hapticController
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settings by viewModel.settings.collectAsStateWithLifecycle()
            val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
            val showVipPaywall by viewModel.showVipPaywall.collectAsStateWithLifecycle()
            val vipPaywallReason by viewModel.vipPaywallReason.collectAsStateWithLifecycle()

            // Keep screen on control
            LaunchedEffect(settings.keepScreenOn) {
                if (settings.keepScreenOn) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }

            ZikirmatikTheme(
                themeId = settings.themeId,
                themeMode = settings.themeMode
            ) {
                val navItems = listOf(
                    NavTabItem(
                        screen = AppNavScreen.COUNTER,
                        label = "Sayaç",
                        selectedIcon = Icons.Filled.RadioButtonChecked,
                        unselectedIcon = Icons.Outlined.RadioButtonChecked
                    ),
                    NavTabItem(
                        screen = AppNavScreen.LIBRARY,
                        label = "Zikirler",
                        selectedIcon = Icons.Filled.FormatListBulleted,
                        unselectedIcon = Icons.Outlined.FormatListBulleted
                    ),
                    NavTabItem(
                        screen = AppNavScreen.STATISTICS,
                        label = "İstatistik",
                        selectedIcon = Icons.Filled.BarChart,
                        unselectedIcon = Icons.Outlined.BarChart
                    ),
                    NavTabItem(
                        screen = AppNavScreen.THEMES,
                        label = "Temalar",
                        selectedIcon = Icons.Filled.ColorLens,
                        unselectedIcon = Icons.Outlined.ColorLens
                    ),
                    NavTabItem(
                        screen = AppNavScreen.SETTINGS,
                        label = "Ayarlar",
                        selectedIcon = Icons.Filled.Settings,
                        unselectedIcon = Icons.Outlined.Settings
                    )
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = when (currentScreen) {
                                        AppNavScreen.COUNTER -> "Zikirmatik"
                                        AppNavScreen.LIBRARY -> "Zikir & Dua Kütüphanesi"
                                        AppNavScreen.STATISTICS -> "Zikir İstatistikleri"
                                        AppNavScreen.THEMES -> "Özgün Temalar"
                                        AppNavScreen.SETTINGS -> "Ayarlar"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            actions = {
                                if (settings.isVipActive) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFFD4AF37).copy(alpha = 0.2f),
                                        modifier = Modifier
                                            .padding(end = 12.dp)
                                            .clickable { viewModel.openVipPaywall("VIP Üyelik Ayrıcalıklarınız") }
                                            .testTag("top_vip_badge")
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.WorkspacePremium,
                                                contentDescription = "VIP Aktif",
                                                tint = Color(0xFF9E7815),
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = "VIP",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = Color(0xFF9E7815)
                                            )
                                        }
                                    }
                                } else {
                                    IconButton(
                                        onClick = { viewModel.openVipPaywall("Zikirmatik VIP Ayrıcalıkları") },
                                        modifier = Modifier
                                            .padding(end = 6.dp)
                                            .testTag("top_vip_action_button")
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    Brush.radialGradient(
                                                        listOf(Color(0xFFFFDF7D), Color(0xFFC79A1E))
                                                    )
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.WorkspacePremium,
                                                contentDescription = "VIP Yükselt",
                                                tint = Color.White,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier
                                .windowInsetsPadding(WindowInsets.navigationBars)
                                .testTag("bottom_nav_bar"),
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 6.dp
                        ) {
                            navItems.forEach { tab ->
                                val isSelected = currentScreen == tab.screen
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { viewModel.navigateTo(tab.screen) },
                                    icon = {
                                        Icon(
                                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = tab.label,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = tab.label,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    modifier = Modifier.testTag("nav_tab_${tab.screen.route}")
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Crossfade(
                        targetState = currentScreen,
                        label = "screen_transition"
                    ) { screen ->
                        when (screen) {
                            AppNavScreen.COUNTER -> MainCounterScreen(
                                viewModel = viewModel,
                                contentPadding = innerPadding
                            )
                            AppNavScreen.LIBRARY -> DhikrLibraryScreen(
                                viewModel = viewModel,
                                contentPadding = innerPadding
                            )
                            AppNavScreen.STATISTICS -> StatisticsScreen(
                                viewModel = viewModel,
                                contentPadding = innerPadding
                            )
                            AppNavScreen.THEMES -> ThemesScreen(
                                viewModel = viewModel,
                                contentPadding = innerPadding
                            )
                            AppNavScreen.SETTINGS -> SettingsScreen(
                                viewModel = viewModel,
                                contentPadding = innerPadding
                            )
                        }
                    }

                    // VIP Paywall Dialog
                    if (showVipPaywall) {
                        VipPaywallDialog(
                            isVipActive = settings.isVipActive,
                            currentPlanName = settings.vipPlanName,
                            reason = vipPaywallReason,
                            onDismiss = { viewModel.closeVipPaywall() },
                            onSelectPlan = { plan ->
                                viewModel.activateVip(plan.title)
                                viewModel.closeVipPaywall()
                            },
                            onRestorePurchases = {
                                viewModel.activateVip("Geri Yüklenen VIP")
                                viewModel.closeVipPaywall()
                            },
                            onRedeemPromo = { code ->
                                viewModel.redeemPromoCode(code)
                            }
                        )
                    }
                }
            }
        }
    }
}
