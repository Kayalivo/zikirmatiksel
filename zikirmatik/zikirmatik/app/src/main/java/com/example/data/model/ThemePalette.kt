package com.example.data.model

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

enum class ThemeModeOption(val id: String, val title: String) {
    SYSTEM("system", "Sistem Varsayılanı"),
    LIGHT("light", "Açık Tema"),
    DARK("dark", "Koyu Tema")
}

data class ZikirTheme(
    val id: String,
    val name: String,
    val subtitle: String,
    val previewPrimary: Color,
    val previewSecondary: Color,
    val previewBackground: Color,
    val isOled: Boolean = false,
    val isVip: Boolean = false,
    val lightColorScheme: ColorScheme,
    val darkColorScheme: ColorScheme
)

object ThemePalettes {
    // 1. Zümrüt & Altın (Classic Islamic Emerald & Gold - Free)
    val Emerald = ZikirTheme(
        id = "emerald",
        name = "Zümrüt & Altın",
        subtitle = "Klasik İslami yeşil ve altın parıltısı",
        previewPrimary = Color(0xFF007953),
        previewSecondary = Color(0xFFD4AF37),
        previewBackground = Color(0xFF0F2B20),
        isVip = false,
        lightColorScheme = lightColorScheme(
            primary = Color(0xFF006C4A),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFF8CF5C6),
            onPrimaryContainer = Color(0xFF002114),
            secondary = Color(0xFFB8860B),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFFFDF9E),
            onSecondaryContainer = Color(0xFF261A00),
            tertiary = Color(0xFF386568),
            onTertiary = Color(0xFFFFFFFF),
            background = Color(0xFFF6FBF6),
            onBackground = Color(0xFF191D1A),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF191D1A),
            surfaceVariant = Color(0xFFDBE5DE),
            onSurfaceVariant = Color(0xFF404944),
            outline = Color(0xFF707973)
        ),
        darkColorScheme = darkColorScheme(
            primary = Color(0xFF6FDBAB),
            onPrimary = Color(0xFF003824),
            primaryContainer = Color(0xFF005237),
            onPrimaryContainer = Color(0xFF8CF5C6),
            secondary = Color(0xFFF5C04A),
            onSecondary = Color(0xFF422C00),
            secondaryContainer = Color(0xFF5F4100),
            onSecondaryContainer = Color(0xFFFFDF9E),
            tertiary = Color(0xFFA0CFD2),
            onTertiary = Color(0xFF003639),
            background = Color(0xFF0C1612),
            onBackground = Color(0xFFE1E4E0),
            surface = Color(0xFF14211C),
            onSurface = Color(0xFFE1E4E0),
            surfaceVariant = Color(0xFF1F3029),
            onSurfaceVariant = Color(0xFFBFC9C2),
            outline = Color(0xFF8A938D)
        )
    )

    // 2. Kâbe & Asil Altın (Regal Deep Black & Gold - VIP)
    val Kaaba = ZikirTheme(
        id = "kaaba",
        name = "Kâbe & Asil Altın",
        subtitle = "Gece siyahı zemin ve Kâbe örtüsü altın yaldızları",
        previewPrimary = Color(0xFFD4AF37),
        previewSecondary = Color(0xFFE6CA65),
        previewBackground = Color(0xFF050505),
        isOled = true,
        isVip = true,
        lightColorScheme = lightColorScheme(
            primary = Color(0xFF806400),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFFFE088),
            onPrimaryContainer = Color(0xFF271D00),
            secondary = Color(0xFF6D5E3F),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFF7E2BB),
            onSecondaryContainer = Color(0xFF261B04),
            tertiary = Color(0xFF4B6546),
            onTertiary = Color(0xFFFFFFFF),
            background = Color(0xFFFDFCFA),
            onBackground = Color(0xFF1E1B16),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF1E1B16),
            surfaceVariant = Color(0xFFEDE1CF),
            onSurfaceVariant = Color(0xFF4D4639),
            outline = Color(0xFF7F7667)
        ),
        darkColorScheme = darkColorScheme(
            primary = Color(0xFFECC244),
            onPrimary = Color(0xFF3F3000),
            primaryContainer = Color(0xFF5B4600),
            onPrimaryContainer = Color(0xFFFFE088),
            secondary = Color(0xFFDAC6A1),
            onSecondary = Color(0xFF3B2F15),
            secondaryContainer = Color(0xFF53462A),
            onSecondaryContainer = Color(0xFFF7E2BB),
            tertiary = Color(0xFFB1CCAC),
            onTertiary = Color(0xFF1D361B),
            background = Color(0xFF000000),
            onBackground = Color(0xFFE8E2D9),
            surface = Color(0xFF101010),
            onSurface = Color(0xFFE8E2D9),
            surfaceVariant = Color(0xFF1D1A14),
            onSurfaceVariant = Color(0xFFD0C5B4),
            outline = Color(0xFF998F80)
        )
    )

    // 3. Osmanlı Turkuazı (Ottoman Turquoise & Cobalt - VIP)
    val Turquoise = ZikirTheme(
        id = "turquoise",
        name = "Osmanlı Turkuazı",
        subtitle = "İznik çinisi mavisi ve ferahlatıcı turkuaz",
        previewPrimary = Color(0xFF00897B),
        previewSecondary = Color(0xFF00ACC1),
        previewBackground = Color(0xFF0A1F24),
        isVip = true,
        lightColorScheme = lightColorScheme(
            primary = Color(0xFF006A60),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFF73F8E7),
            onPrimaryContainer = Color(0xFF00201C),
            secondary = Color(0xFF00677C),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFB2EBFF),
            onSecondaryContainer = Color(0xFF001F27),
            tertiary = Color(0xFF4A6363),
            onTertiary = Color(0xFFFFFFFF),
            background = Color(0xFFF4FBFA),
            onBackground = Color(0xFF161D1C),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF161D1C),
            surfaceVariant = Color(0xFFDAE5E3),
            onSurfaceVariant = Color(0xFF3F4947),
            outline = Color(0xFF6F7978)
        ),
        darkColorScheme = darkColorScheme(
            primary = Color(0xFF53DBCB),
            onPrimary = Color(0xFF003731),
            primaryContainer = Color(0xFF005048),
            onPrimaryContainer = Color(0xFF73F8E7),
            secondary = Color(0xFF85D2E9),
            onSecondary = Color(0xFF003541),
            secondaryContainer = Color(0xFF004D5E),
            onSecondaryContainer = Color(0xFFB2EBFF),
            tertiary = Color(0xFFB1CCCC),
            onTertiary = Color(0xFF1C3534),
            background = Color(0xFF0B1617),
            onBackground = Color(0xFFDFE4E3),
            surface = Color(0xFF122224),
            onSurface = Color(0xFFDFE4E3),
            surfaceVariant = Color(0xFF1B2F31),
            onSurfaceVariant = Color(0xFFBEC9C7),
            outline = Color(0xFF889391)
        )
    )

    // 4. Gül Bahçesi & Kehribar (Rose & Amber Serenity - VIP)
    val Rose = ZikirTheme(
        id = "rose",
        name = "Gül Bahçesi",
        subtitle = "Peygamber gülü zarafeti ve huzurlu tonlar",
        previewPrimary = Color(0xFFB8455A),
        previewSecondary = Color(0xFFD48B80),
        previewBackground = Color(0xFF241014),
        isVip = true,
        lightColorScheme = lightColorScheme(
            primary = Color(0xFF984055),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFFFD9DF),
            onPrimaryContainer = Color(0xFF3E0017),
            secondary = Color(0xFF8F4C38),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFFFDBCF),
            onSecondaryContainer = Color(0xFF380D00),
            tertiary = Color(0xFF7A583E),
            onTertiary = Color(0xFFFFFFFF),
            background = Color(0xFFFFF8F8),
            onBackground = Color(0xFF21191B),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF21191B),
            surfaceVariant = Color(0xFFF3DDE0),
            onSurfaceVariant = Color(0xFF524346),
            outline = Color(0xFF847376)
        ),
        darkColorScheme = darkColorScheme(
            primary = Color(0xFFFFB1C1),
            onPrimary = Color(0xFF5E1128),
            primaryContainer = Color(0xFF7B293E),
            onPrimaryContainer = Color(0xFFFFD9DF),
            secondary = Color(0xFFFFB5A0),
            onSecondary = Color(0xFF552010),
            secondaryContainer = Color(0xFF723523),
            onSecondaryContainer = Color(0xFFFFDBCF),
            tertiary = Color(0xFFECC0A2),
            onTertiary = Color(0xFF462A15),
            background = Color(0xFF170C0F),
            onBackground = Color(0xFFEBE0E1),
            surface = Color(0xFF231418),
            onSurface = Color(0xFFEBE0E1),
            surfaceVariant = Color(0xFF341E24),
            onSurfaceVariant = Color(0xFFD6C2C5),
            outline = Color(0xFF9F8C8F)
        )
    )

    // 5. Gök Kubbe & Safir (Midnight Sky & Blue - VIP)
    val Sky = ZikirTheme(
        id = "sky",
        name = "Gök Kubbe",
        subtitle = "Gece gökyüzü, yıldızlar ve safir dinginliği",
        previewPrimary = Color(0xFF1976D2),
        previewSecondary = Color(0xFF64B5F6),
        previewBackground = Color(0xFF091428),
        isVip = true,
        lightColorScheme = lightColorScheme(
            primary = Color(0xFF0061A4),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFD1E4FF),
            onPrimaryContainer = Color(0xFF001D36),
            secondary = Color(0xFF006782),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFBAEAFF),
            onSecondaryContainer = Color(0xFF001F29),
            tertiary = Color(0xFF5B5B7E),
            onTertiary = Color(0xFFFFFFFF),
            background = Color(0xFFF7F9FF),
            onBackground = Color(0xFF181C20),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF181C20),
            surfaceVariant = Color(0xFFDFE2EB),
            onSurfaceVariant = Color(0xFF42474E),
            outline = Color(0xFF73777F)
        ),
        darkColorScheme = darkColorScheme(
            primary = Color(0xFF9ECAFF),
            onPrimary = Color(0xFF003258),
            primaryContainer = Color(0xFF00497D),
            onPrimaryContainer = Color(0xFFD1E4FF),
            secondary = Color(0xFF5FD4FD),
            onSecondary = Color(0xFF003544),
            secondaryContainer = Color(0xFF004D62),
            onSecondaryContainer = Color(0xFFBAEAFF),
            tertiary = Color(0xFFC4C3EA),
            onTertiary = Color(0xFF2D2D4D),
            background = Color(0xFF0A101D),
            onBackground = Color(0xFFDFE2EB),
            surface = Color(0xFF121B2C),
            onSurface = Color(0xFFDFE2EB),
            surfaceVariant = Color(0xFF1D283E),
            onSurfaceVariant = Color(0xFFC3C7D0),
            outline = Color(0xFF8D9199)
        )
    )

    // 6. Doğal Ahşap & Kehribar (Handcrafted Walnut Tasbih - VIP)
    val Wood = ZikirTheme(
        id = "wood",
        name = "Ahşap & Kehribar",
        subtitle = "Geleneksel kuka ve öd ağacı tesbih hissi",
        previewPrimary = Color(0xFF8D5B28),
        previewSecondary = Color(0xFFC88232),
        previewBackground = Color(0xFF23170E),
        isVip = true,
        lightColorScheme = lightColorScheme(
            primary = Color(0xFF8B5014),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFFFDCC1),
            onPrimaryContainer = Color(0xFF2E1500),
            secondary = Color(0xFF745A43),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFFFDECD),
            onSecondaryContainer = Color(0xFF2A1706),
            tertiary = Color(0xFF5B633B),
            onTertiary = Color(0xFFFFFFFF),
            background = Color(0xFFFFF8F5),
            onBackground = Color(0xFF211A15),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF211A15),
            surfaceVariant = Color(0xFFF3DFC8),
            onSurfaceVariant = Color(0xFF51453A),
            outline = Color(0xFF847469)
        ),
        darkColorScheme = darkColorScheme(
            primary = Color(0xFFFFB77C),
            onPrimary = Color(0xFF4D2600),
            primaryContainer = Color(0xFF6C3B00),
            onPrimaryContainer = Color(0xFFFFDCC1),
            secondary = Color(0xFFE4BEA0),
            onSecondary = Color(0xFF422C19),
            secondaryContainer = Color(0xFF5B422D),
            onSecondaryContainer = Color(0xFFFFDECD),
            tertiary = Color(0xFFC3CB9E),
            onTertiary = Color(0xFF2E3411),
            background = Color(0xFF18120D),
            onBackground = Color(0xFFECE0DA),
            surface = Color(0xFF241C15),
            onSurface = Color(0xFFECE0DA),
            surfaceVariant = Color(0xFF382D23),
            onSurfaceVariant = Color(0xFFD6C3B7),
            outline = Color(0xFF9E8D82)
        )
    )

    // 7. Minimal Titanyum (Monochrome Modern - Free)
    val Minimal = ZikirTheme(
        id = "minimal",
        name = "Minimal Titanyum",
        subtitle = "Sade, modern ve odaklanmayı kolaylaştıran tasarım",
        previewPrimary = Color(0xFF5A626A),
        previewSecondary = Color(0xFF78838E),
        previewBackground = Color(0xFF121417),
        isVip = false,
        lightColorScheme = lightColorScheme(
            primary = Color(0xFF4A5568),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFDCE2EC),
            onPrimaryContainer = Color(0xFF0F1822),
            secondary = Color(0xFF5D636E),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFE2E4EE),
            onSecondaryContainer = Color(0xFF1A1F28),
            tertiary = Color(0xFF495B6A),
            onTertiary = Color(0xFFFFFFFF),
            background = Color(0xFFF8F9FA),
            onBackground = Color(0xFF1A1C1E),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF1A1C1E),
            surfaceVariant = Color(0xFFDFE2E8),
            onSurfaceVariant = Color(0xFF43474E),
            outline = Color(0xFF73777F)
        ),
        darkColorScheme = darkColorScheme(
            primary = Color(0xFFBCC7D8),
            onPrimary = Color(0xFF27313F),
            primaryContainer = Color(0xFF3E4856),
            onPrimaryContainer = Color(0xFFDCE2EC),
            secondary = Color(0xFFC6CBD8),
            onSecondary = Color(0xFF2F343E),
            secondaryContainer = Color(0xFF464B56),
            onSecondaryContainer = Color(0xFFE2E4EE),
            tertiary = Color(0xFFB2C6D8),
            onTertiary = Color(0xFF1C3140),
            background = Color(0xFF101216),
            onBackground = Color(0xFFE2E2E6),
            surface = Color(0xFF181B20),
            onSurface = Color(0xFFE2E2E6),
            surfaceVariant = Color(0xFF262A32),
            onSurfaceVariant = Color(0xFFC3C7CF),
            outline = Color(0xFF8D9199)
        )
    )

    val allThemes = listOf(Emerald, Kaaba, Turquoise, Rose, Sky, Wood, Minimal)

    fun getThemeById(id: String): ZikirTheme {
        return allThemes.find { it.id == id } ?: Emerald
    }
}
