package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.db.DhikrEntity
import com.example.data.db.DhikrHistoryEntity
import com.example.data.model.DhikrCategory
import com.example.data.model.ThemeModeOption
import com.example.data.repository.DayStatItem
import com.example.data.repository.DhikrRepository
import com.example.data.repository.SettingsRepository
import com.example.haptics.HapticController
import com.example.haptics.VibrationStrength
import com.example.sound.SoundPlayer
import com.example.sound.SoundType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppNavScreen(val route: String, val title: String) {
    COUNTER("counter", "Sayaç"),
    LIBRARY("library", "Zikirler"),
    STATISTICS("statistics", "İstatistik"),
    THEMES("themes", "Temalar"),
    SETTINGS("settings", "Ayarlar")
}

class ZikirmatikViewModel(
    private val dhikrRepository: DhikrRepository,
    private val settingsRepository: SettingsRepository,
    private val soundPlayer: SoundPlayer,
    private val hapticController: HapticController
) : ViewModel() {

    val settings = settingsRepository.settings

    val allDhikrs: StateFlow<List<DhikrEntity>> = dhikrRepository.allDhikrs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentHistory: StateFlow<List<DhikrHistoryEntity>> = dhikrRepository.recentHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayCount: StateFlow<Int> = dhikrRepository.getTodayTotalCount()
        .combine(MutableStateFlow(0)) { c, _ -> c ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val weekCount: StateFlow<Int> = dhikrRepository.getWeekTotalCount()
        .combine(MutableStateFlow(0)) { c, _ -> c ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val monthCount: StateFlow<Int> = dhikrRepository.getMonthTotalCount()
        .combine(MutableStateFlow(0)) { c, _ -> c ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val allTimeCount: StateFlow<Int> = dhikrRepository.getAllTimeCount()
        .combine(MutableStateFlow(0)) { c, _ -> c ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _currentScreen = MutableStateFlow(AppNavScreen.COUNTER)
    val currentScreen: StateFlow<AppNavScreen> = _currentScreen.asStateFlow()

    private val _selectedCategory = MutableStateFlow(DhikrCategory.ALL)
    val selectedCategory: StateFlow<DhikrCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _activeDhikr = MutableStateFlow<DhikrEntity?>(null)
    val activeDhikr: StateFlow<DhikrEntity?> = _activeDhikr.asStateFlow()

    private val _sevenDaysStats = MutableStateFlow<List<DayStatItem>>(emptyList())
    val sevenDaysStats: StateFlow<List<DayStatItem>> = _sevenDaysStats.asStateFlow()

    private val _streakCount = MutableStateFlow(0)
    val streakCount: StateFlow<Int> = _streakCount.asStateFlow()

    // Lap/cycle tracking in current session
    private val _targetReachedAnimation = MutableStateFlow(false)
    val targetReachedAnimation: StateFlow<Boolean> = _targetReachedAnimation.asStateFlow()

    // VIP Paywall Dialog state
    private val _showVipPaywall = MutableStateFlow(false)
    val showVipPaywall: StateFlow<Boolean> = _showVipPaywall.asStateFlow()

    private val _vipPaywallReason = MutableStateFlow<String?>(null)
    val vipPaywallReason: StateFlow<String?> = _vipPaywallReason.asStateFlow()

    init {
        viewModelScope.launch {
            allDhikrs.collect { list ->
                if (list.isNotEmpty()) {
                    val activeId = settings.value.activeDhikrId
                    val found = list.find { it.id == activeId } ?: list.first()
                    _activeDhikr.value = found
                }
            }
        }
        refreshStats()
    }

    fun navigateTo(screen: AppNavScreen) {
        _currentScreen.value = screen
        if (screen == AppNavScreen.STATISTICS) {
            refreshStats()
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategory(category: DhikrCategory) {
        _selectedCategory.value = category
    }

    fun selectActiveDhikr(dhikr: DhikrEntity) {
        _activeDhikr.value = dhikr
        settingsRepository.setActiveDhikrId(dhikr.id)
        _currentScreen.value = AppNavScreen.COUNTER
    }

    fun incrementActiveDhikr() {
        val current = _activeDhikr.value ?: return
        val currentSettings = settings.value

        // Haptic feedback
        hapticController.vibrateClick(currentSettings.vibrationStrength)

        // Sound feedback
        soundPlayer.playClick(currentSettings.soundType)

        viewModelScope.launch {
            val newCount = dhikrRepository.incrementDhikr(current.id, current)
            _activeDhikr.value = current.copy(
                currentCount = newCount,
                totalRecited = current.totalRecited + 1
            )

            // Check target completion
            if (current.targetCount > 0 && newCount % current.targetCount == 0) {
                if (currentSettings.vibrateOnTarget) {
                    hapticController.vibrateTargetComplete()
                }
                if (currentSettings.soundOnTarget) {
                    soundPlayer.playTargetComplete()
                }
                _targetReachedAnimation.value = true
            }
        }
    }

    fun dismissTargetAnimation() {
        _targetReachedAnimation.value = false
    }

    fun decrementActiveDhikr() {
        val current = _activeDhikr.value ?: return
        if (current.currentCount <= 0) return

        hapticController.vibrateClick(VibrationStrength.LIGHT)

        viewModelScope.launch {
            val newCount = dhikrRepository.decrementDhikr(current.id, current)
            _activeDhikr.value = current.copy(
                currentCount = newCount,
                totalRecited = maxOf(0, current.totalRecited - 1)
            )
        }
    }

    fun resetActiveDhikr() {
        val current = _activeDhikr.value ?: return
        hapticController.vibrateClick(VibrationStrength.MEDIUM)

        viewModelScope.launch {
            dhikrRepository.resetDhikr(current.id)
            _activeDhikr.value = current.copy(currentCount = 0)
        }
    }

    fun updateActiveDhikrTarget(target: Int) {
        val current = _activeDhikr.value ?: return
        viewModelScope.launch {
            val updated = current.copy(targetCount = target)
            dhikrRepository.updateDhikr(updated)
            _activeDhikr.value = updated
        }
    }

    fun toggleFavorite(dhikr: DhikrEntity) {
        viewModelScope.launch {
            dhikrRepository.toggleFavorite(dhikr.id, dhikr.isFavorite)
            if (_activeDhikr.value?.id == dhikr.id) {
                _activeDhikr.value = _activeDhikr.value?.copy(isFavorite = !dhikr.isFavorite)
            }
        }
    }

    fun addCustomDhikr(
        title: String,
        arabicText: String,
        transliteration: String,
        meaning: String,
        targetCount: Int,
        category: String
    ) {
        viewModelScope.launch {
            val newDhikr = DhikrEntity(
                title = title.trim(),
                arabicText = arabicText.trim(),
                transliteration = transliteration.trim(),
                meaning = meaning.trim(),
                category = category,
                targetCount = targetCount,
                currentCount = 0,
                totalRecited = 0,
                isCustom = true,
                isFavorite = false,
                orderIndex = 100
            )
            val id = dhikrRepository.insertDhikr(newDhikr)
            val created = newDhikr.copy(id = id)
            selectActiveDhikr(created)
        }
    }

    fun updateCustomDhikr(dhikr: DhikrEntity) {
        viewModelScope.launch {
            dhikrRepository.updateDhikr(dhikr)
            if (_activeDhikr.value?.id == dhikr.id) {
                _activeDhikr.value = dhikr
            }
        }
    }

    fun deleteDhikr(dhikr: DhikrEntity) {
        viewModelScope.launch {
            dhikrRepository.deleteDhikr(dhikr)
            if (_activeDhikr.value?.id == dhikr.id) {
                val list = allDhikrs.value.filter { it.id != dhikr.id }
                if (list.isNotEmpty()) {
                    selectActiveDhikr(list.first())
                }
            }
        }
    }

    fun refreshStats() {
        viewModelScope.launch {
            _sevenDaysStats.value = dhikrRepository.getDailyAggregatesForLastDays(7)
            _streakCount.value = dhikrRepository.calculateStreak()
        }
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            dhikrRepository.deleteHistoryItem(id)
            refreshStats()
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            dhikrRepository.clearHistory()
            refreshStats()
        }
    }

    // Settings modifiers
    fun setThemeId(themeId: String) = settingsRepository.setThemeId(themeId)
    fun setThemeMode(mode: ThemeModeOption) = settingsRepository.setThemeMode(mode)
    fun setVibrationStrength(strength: VibrationStrength) = settingsRepository.setVibrationStrength(strength)
    fun setSoundType(sound: SoundType) = settingsRepository.setSoundType(sound)
    fun setCounterMode(mode: com.example.data.model.CounterMode) = settingsRepository.setCounterMode(mode)
    fun setDeviceSkin(skin: com.example.data.model.DeviceSkin) = settingsRepository.setDeviceSkin(skin)
    fun setKeepScreenOn(enabled: Boolean) = settingsRepository.setKeepScreenOn(enabled)
    fun setFullscreenMode(enabled: Boolean) = settingsRepository.setFullscreenMode(enabled)
    fun setVibrateOnTarget(enabled: Boolean) = settingsRepository.setVibrateOnTarget(enabled)
    fun setSoundOnTarget(enabled: Boolean) = settingsRepository.setSoundOnTarget(enabled)

    // VIP Management
    fun openVipPaywall(reason: String? = null) {
        _vipPaywallReason.value = reason
        _showVipPaywall.value = true
    }

    fun closeVipPaywall() {
        _showVipPaywall.value = false
        _vipPaywallReason.value = null
    }

    fun activateVip(planName: String = "Ömür Boyu VIP") {
        settingsRepository.setVipActive(true, planName)
    }

    fun deactivateVip() {
        settingsRepository.setVipActive(false, "")
    }

    fun setReminderFriday(enabled: Boolean) {
        if (!settings.value.isVipActive && enabled) {
            openVipPaywall("Cuma Salavatı & Kehf Hatırlatıcısı VIP özelliğidir.")
            return
        }
        settingsRepository.setReminderFriday(enabled)
    }

    fun setReminderMorningEvening(enabled: Boolean) {
        if (!settings.value.isVipActive && enabled) {
            openVipPaywall("Sabah & Akşam Virdi Hatırlatıcısı VIP özelliğidir.")
            return
        }
        settingsRepository.setReminderMorningEvening(enabled)
    }

    fun setReminderTahajjud(enabled: Boolean) {
        if (!settings.value.isVipActive && enabled) {
            openVipPaywall("Teheccüd & İstiğfar Hatırlatıcısı VIP özelliğidir.")
            return
        }
        settingsRepository.setReminderTahajjud(enabled)
    }
}

class ZikirmatikViewModelFactory(
    private val dhikrRepository: DhikrRepository,
    private val settingsRepository: SettingsRepository,
    private val soundPlayer: SoundPlayer,
    private val hapticController: HapticController
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ZikirmatikViewModel::class.java)) {
            return ZikirmatikViewModel(
                dhikrRepository,
                settingsRepository,
                soundPlayer,
                hapticController
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
