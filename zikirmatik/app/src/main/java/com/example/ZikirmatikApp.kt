package com.example

import android.app.Application
import com.example.data.db.AppDatabase
import com.example.data.repository.DhikrRepository
import com.example.data.repository.SettingsRepository
import com.example.haptics.HapticController
import com.example.sound.SoundPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ZikirmatikApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val dhikrRepository by lazy { DhikrRepository(database.dhikrDao()) }
    val settingsRepository by lazy { SettingsRepository(this) }
    val soundPlayer by lazy { SoundPlayer() }
    val hapticController by lazy { HapticController(this) }

    override fun onCreate() {
        super.onCreate()
        // Seed presets in background if needed
        CoroutineScope(Dispatchers.IO).launch {
            dhikrRepository.checkAndSeedPresets()
        }
    }
}
