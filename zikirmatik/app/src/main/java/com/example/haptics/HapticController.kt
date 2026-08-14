package com.example.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

enum class VibrationStrength(val id: String, val title: String) {
    LIGHT("light", "Hafif Titreşim"),
    MEDIUM("medium", "Orta Titreşim"),
    STRONG("strong", "Güçlü Titreşim"),
    OFF("off", "Kapalı")
}

class HapticController(context: Context) {
    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    fun vibrateClick(strength: VibrationStrength) {
        if (strength == VibrationStrength.OFF || vibrator == null) return

        try {
            if (!vibrator.hasVibrator()) return

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val (duration, amplitude) = when (strength) {
                    VibrationStrength.LIGHT -> Pair(12L, 60)
                    VibrationStrength.MEDIUM -> Pair(22L, 140)
                    VibrationStrength.STRONG -> Pair(35L, 255)
                    VibrationStrength.OFF -> return
                }
                vibrator.vibrate(VibrationEffect.createOneShot(duration, amplitude))
            } else {
                @Suppress("DEPRECATION")
                val duration = when (strength) {
                    VibrationStrength.LIGHT -> 10L
                    VibrationStrength.MEDIUM -> 20L
                    VibrationStrength.STRONG -> 40L
                    VibrationStrength.OFF -> 0L
                }
                if (duration > 0) {
                    vibrator.vibrate(duration)
                }
            }
        } catch (_: Exception) {
            // Gracefully ignore vibration errors on unsupported hardware or emulators
        }
    }

    fun vibrateTargetComplete() {
        if (vibrator == null) return

        try {
            if (!vibrator.hasVibrator()) return

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 60, 80, 80, 80, 150)
                val amplitudes = intArrayOf(0, 180, 0, 220, 0, 255)
                vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                val pattern = longArrayOf(0, 70, 80, 90, 80, 160)
                vibrator.vibrate(pattern, -1)
            }
        } catch (_: Exception) {
            // Gracefully ignore vibration errors
        }
    }
}
