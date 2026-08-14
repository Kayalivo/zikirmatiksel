package com.example.data.model

enum class CounterMode(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val isVipOnly: Boolean = false
) {
    DIGITAL_RING("digital_ring", "Klasik Zikirmatik", "Otantik metalik yüzük & LCD dijital ekran", "TouchApp", false),
    REAL_BEADS("real_beads", "Hakiki Tesbih Boncukları", "Dokunarak ve kaydırarak çekilen fiziksel taneler", "Grain", false),
    MODERN_HALO("modern_halo", "Modern Halka", "Minimalist, akıcı dalga & çember göstergesi", "RadioButtonChecked", false),
    BLIND_TOUCH("blind_touch", "Kör / Cepte Zikir", "Ekrana bakmadan, tam ekran titreşimli dokunma", "VisibilityOff", false)
}

enum class DeviceSkin(
    val id: String,
    val title: String,
    val primaryColorHex: Long,
    val accentColorHex: Long,
    val isVipOnly: Boolean = false
) {
    EMERALD_GOLD("emerald_gold", "Osmanlı Zümrüt & Altın", 0xFF0D3B2E, 0xFFD4AF37, false),
    KUKA_WOOD("kuka_wood", "Hakiki Kuka & Ahşap", 0xFF3D2314, 0xFFC68B59, true),
    KAABA_BLACK("kaaba_black", "Kâbe Siyahı & Yaldız", 0xFF121212, 0xFFE5C058, true),
    PEARL_WHITE("pearl_white", "Sedef & Firuze", 0xFFF4F6F7, 0xFF00A896, true),
    MIDNIGHT_ROSE("midnight_rose", "Gül-i Muhammedî", 0xFF2B101D, 0xFFE07A5F, true)
}
