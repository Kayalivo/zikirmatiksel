package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.CounterMode
import com.example.data.model.DeviceSkin
import com.example.data.model.ThemeModeOption
import com.example.haptics.VibrationStrength
import com.example.sound.SoundType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppSettingsState(
    val themeId: String = "emerald",
    val themeMode: ThemeModeOption = ThemeModeOption.SYSTEM,
    val vibrationStrength: VibrationStrength = VibrationStrength.MEDIUM,
    val soundType: SoundType = SoundType.CLICK,
    val counterMode: CounterMode = CounterMode.DIGITAL_RING,
    val deviceSkin: DeviceSkin = DeviceSkin.EMERALD_GOLD,
    val keepScreenOn: Boolean = true,
    val fullscreenMode: Boolean = false,
    val vibrateOnTarget: Boolean = true,
    val soundOnTarget: Boolean = true,
    val activeDhikrId: Long = 1L,
    val isVipActive: Boolean = false,
    val vipPlanName: String = "",
    val reminderFriday: Boolean = false,
    val reminderMorningEvening: Boolean = false,
    val reminderTahajjud: Boolean = false
)

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("zikirmatik_prefs", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<AppSettingsState> = _settings.asStateFlow()

    private fun loadSettings(): AppSettingsState {
        val themeId = prefs.getString("theme_id", "emerald") ?: "emerald"
        val themeModeStr = prefs.getString("theme_mode", ThemeModeOption.SYSTEM.id) ?: ThemeModeOption.SYSTEM.id
        val vibStr = prefs.getString("vibration_strength", VibrationStrength.MEDIUM.id) ?: VibrationStrength.MEDIUM.id
        val soundStr = prefs.getString("sound_type", SoundType.CLICK.id) ?: SoundType.CLICK.id
        val modeStr = prefs.getString("counter_mode", CounterMode.DIGITAL_RING.id) ?: CounterMode.DIGITAL_RING.id
        val skinStr = prefs.getString("device_skin", DeviceSkin.EMERALD_GOLD.id) ?: DeviceSkin.EMERALD_GOLD.id
        val keepScreenOn = prefs.getBoolean("keep_screen_on", true)
        val fullscreenMode = prefs.getBoolean("fullscreen_mode", false)
        val vibrateOnTarget = prefs.getBoolean("vibrate_on_target", true)
        val soundOnTarget = prefs.getBoolean("sound_on_target", true)
        val activeDhikrId = prefs.getLong("active_dhikr_id", 1L)
        val isVipActive = prefs.getBoolean("is_vip_active", false)
        val vipPlanName = prefs.getString("vip_plan_name", "") ?: ""
        val reminderFriday = prefs.getBoolean("reminder_friday", false)
        val reminderMorningEvening = prefs.getBoolean("reminder_morning_evening", false)
        val reminderTahajjud = prefs.getBoolean("reminder_tahajjud", false)

        val themeMode = ThemeModeOption.entries.find { it.id == themeModeStr } ?: ThemeModeOption.SYSTEM
        val vibrationStrength = VibrationStrength.entries.find { it.id == vibStr } ?: VibrationStrength.MEDIUM
        val soundType = SoundType.entries.find { it.id == soundStr } ?: SoundType.CLICK
        val counterMode = CounterMode.entries.find { it.id == modeStr } ?: CounterMode.DIGITAL_RING
        val deviceSkin = DeviceSkin.entries.find { it.id == skinStr } ?: DeviceSkin.EMERALD_GOLD

        return AppSettingsState(
            themeId = themeId,
            themeMode = themeMode,
            vibrationStrength = vibrationStrength,
            soundType = soundType,
            counterMode = counterMode,
            deviceSkin = deviceSkin,
            keepScreenOn = keepScreenOn,
            fullscreenMode = fullscreenMode,
            vibrateOnTarget = vibrateOnTarget,
            soundOnTarget = soundOnTarget,
            activeDhikrId = activeDhikrId,
            isVipActive = isVipActive,
            vipPlanName = vipPlanName,
            reminderFriday = reminderFriday,
            reminderMorningEvening = reminderMorningEvening,
            reminderTahajjud = reminderTahajjud
        )
    }

    fun setCounterMode(mode: CounterMode) {
        prefs.edit().putString("counter_mode", mode.id).apply()
        _settings.value = _settings.value.copy(counterMode = mode)
    }

    fun setDeviceSkin(skin: DeviceSkin) {
        prefs.edit().putString("device_skin", skin.id).apply()
        _settings.value = _settings.value.copy(deviceSkin = skin)
    }

    fun setThemeId(themeId: String) {
        prefs.edit().putString("theme_id", themeId).apply()
        _settings.value = _settings.value.copy(themeId = themeId)
    }

    fun setThemeMode(mode: ThemeModeOption) {
        prefs.edit().putString("theme_mode", mode.id).apply()
        _settings.value = _settings.value.copy(themeMode = mode)
    }

    fun setVibrationStrength(strength: VibrationStrength) {
        prefs.edit().putString("vibration_strength", strength.id).apply()
        _settings.value = _settings.value.copy(vibrationStrength = strength)
    }

    fun setSoundType(sound: SoundType) {
        prefs.edit().putString("sound_type", sound.id).apply()
        _settings.value = _settings.value.copy(soundType = sound)
    }

    fun setKeepScreenOn(enabled: Boolean) {
        prefs.edit().putBoolean("keep_screen_on", enabled).apply()
        _settings.value = _settings.value.copy(keepScreenOn = enabled)
    }

    fun setFullscreenMode(enabled: Boolean) {
        prefs.edit().putBoolean("fullscreen_mode", enabled).apply()
        _settings.value = _settings.value.copy(fullscreenMode = enabled)
    }

    fun setVibrateOnTarget(enabled: Boolean) {
        prefs.edit().putBoolean("vibrate_on_target", enabled).apply()
        _settings.value = _settings.value.copy(vibrateOnTarget = enabled)
    }

    fun setSoundOnTarget(enabled: Boolean) {
        prefs.edit().putBoolean("sound_on_target", enabled).apply()
        _settings.value = _settings.value.copy(soundOnTarget = enabled)
    }

    fun setActiveDhikrId(id: Long) {
        prefs.edit().putLong("active_dhikr_id", id).apply()
        _settings.value = _settings.value.copy(activeDhikrId = id)
    }

    fun setVipActive(active: Boolean, planName: String = "Ömür Boyu VIP") {
        prefs.edit().putBoolean("is_vip_active", active).putString("vip_plan_name", planName).apply()
        _settings.value = _settings.value.copy(isVipActive = active, vipPlanName = planName)
    }

    fun setReminderFriday(enabled: Boolean) {
        prefs.edit().putBoolean("reminder_friday", enabled).apply()
        _settings.value = _settings.value.copy(reminderFriday = enabled)
    }

    fun setReminderMorningEvening(enabled: Boolean) {
        prefs.edit().putBoolean("reminder_morning_evening", enabled).apply()
        _settings.value = _settings.value.copy(reminderMorningEvening = enabled)
    }

    fun setReminderTahajjud(enabled: Boolean) {
        prefs.edit().putBoolean("reminder_tahajjud", enabled).apply()
        _settings.value = _settings.value.copy(reminderTahajjud = enabled)
    }
}
