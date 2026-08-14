package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.data.model.ThemeModeOption
import com.example.data.model.ThemePalettes

val ZikirShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun ZikirmatikTheme(
    themeId: String = "emerald",
    themeMode: ThemeModeOption = ThemeModeOption.SYSTEM,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeModeOption.SYSTEM -> isSystemDark
        ThemeModeOption.LIGHT -> false
        ThemeModeOption.DARK -> true
    }

    val selectedTheme = ThemePalettes.getThemeById(themeId)
    val colorScheme = if (isDark) selectedTheme.darkColorScheme else selectedTheme.lightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = ZikirShapes,
        content = content
    )
}
