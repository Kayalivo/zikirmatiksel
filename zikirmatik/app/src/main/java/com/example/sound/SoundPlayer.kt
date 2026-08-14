package com.example.sound

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

enum class SoundType(val id: String, val title: String, val isVip: Boolean = false) {
    CLICK("click", "Mekanik Tık Sesi", isVip = false),
    OFF("off", "Sessiz", isVip = false),
    KUKA("kuka", "Hakiki Kuka Ahşap Tesbih", isVip = true),
    WATER("water", "Huzurlu Su Damlası", isVip = true),
    BELL("bell", "Sakin Çan & Tını", isVip = true),
    NEY("ney", "Manevi Ney Tınısı", isVip = true)
}

class SoundPlayer {
    private val scope = CoroutineScope(Dispatchers.Default)

    // Pre-generated sound buffers
    private var clickBuffer: ShortArray? = null
    private var kukaBuffer: ShortArray? = null
    private var waterBuffer: ShortArray? = null
    private var bellBuffer: ShortArray? = null
    private var neyBuffer: ShortArray? = null
    private var targetBellBuffer: ShortArray? = null

    private val sampleRate = 44100

    init {
        initBuffers()
    }

    private fun initBuffers() {
        // 1. Click sound: 15ms snappy high frequency burst with exponential decay
        val clickDuration = 0.015
        val clickSamples = (sampleRate * clickDuration).toInt()
        clickBuffer = ShortArray(clickSamples) { i ->
            val t = i.toDouble() / sampleRate
            val decay = exp(-t * 400.0)
            val wave = sin(2 * PI * 1800 * t) * 0.7 + sin(2 * PI * 3600 * t) * 0.3
            (wave * decay * Short.MAX_VALUE * 0.6).toInt().toShort()
        }

        // 2. Kuka Ahsap Tesbih: Rich woody resonance with dual low-mid transients
        val kukaDuration = 0.035
        val kukaSamples = (sampleRate * kukaDuration).toInt()
        kukaBuffer = ShortArray(kukaSamples) { i ->
            val t = i.toDouble() / sampleRate
            val decay = exp(-t * 180.0)
            val wave = sin(2 * PI * 620 * t) * 0.6 + sin(2 * PI * 1240 * t) * 0.3 + sin(2 * PI * 2400 * t) * 0.1
            (wave * decay * Short.MAX_VALUE * 0.75).toInt().toShort()
        }

        // 3. Water droplet: 40ms pitch-bent sine wave (1200Hz dropping to 800Hz)
        val waterDuration = 0.04
        val waterSamples = (sampleRate * waterDuration).toInt()
        waterBuffer = ShortArray(waterSamples) { i ->
            val t = i.toDouble() / sampleRate
            val freq = 1200.0 - (t / waterDuration) * 450.0
            val decay = exp(-t * 80.0)
            val wave = sin(2 * PI * freq * t)
            (wave * decay * Short.MAX_VALUE * 0.7).toInt().toShort()
        }

        // 4. Bell / Chime sound: 60ms gentle bell chime
        val bellDuration = 0.06
        val bellSamples = (sampleRate * bellDuration).toInt()
        bellBuffer = ShortArray(bellSamples) { i ->
            val t = i.toDouble() / sampleRate
            val decay = exp(-t * 40.0)
            val wave = sin(2 * PI * 880 * t) * 0.6 + sin(2 * PI * 1760 * t) * 0.3 + sin(2 * PI * 2640 * t) * 0.1
            (wave * decay * Short.MAX_VALUE * 0.6).toInt().toShort()
        }

        // 5. Ney flute tone: 100ms warm harmonic breath sound
        val neyDuration = 0.10
        val neySamples = (sampleRate * neyDuration).toInt()
        neyBuffer = ShortArray(neySamples) { i ->
            val t = i.toDouble() / sampleRate
            val attack = if (t < 0.02) t / 0.02 else 1.0
            val decay = exp(-(t - 0.02) * 20.0)
            val envelope = attack * if (t >= 0.02) decay else 1.0
            val wave = sin(2 * PI * 440 * t) * 0.6 + sin(2 * PI * 880 * t) * 0.25 + sin(2 * PI * 1320 * t) * 0.15
            (wave * envelope * Short.MAX_VALUE * 0.65).toInt().toShort()
        }

        // 6. Target completion harmony chime: 350ms multi-tone chime
        val targetDuration = 0.35
        val targetSamples = (sampleRate * targetDuration).toInt()
        targetBellBuffer = ShortArray(targetSamples) { i ->
            val t = i.toDouble() / sampleRate
            val decay = exp(-t * 9.0)
            val wave = (sin(2 * PI * 523.25 * t) * 0.4 + // C5
                    sin(2 * PI * 659.25 * t) * 0.3 + // E5
                    sin(2 * PI * 783.99 * t) * 0.3)  // G5
            (wave * decay * Short.MAX_VALUE * 0.8).toInt().toShort()
        }
    }

    fun playClick(soundType: SoundType) {
        if (soundType == SoundType.OFF) return

        val buffer = when (soundType) {
            SoundType.CLICK -> clickBuffer
            SoundType.KUKA -> kukaBuffer
            SoundType.WATER -> waterBuffer
            SoundType.BELL -> bellBuffer
            SoundType.NEY -> neyBuffer
            SoundType.OFF -> null
        } ?: return

        scope.launch {
            playRawBuffer(buffer)
        }
    }

    fun playTargetComplete() {
        val buffer = targetBellBuffer ?: return
        scope.launch {
            playRawBuffer(buffer)
        }
    }

    private fun playRawBuffer(buffer: ShortArray) {
        try {
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()
            Thread.sleep((buffer.size * 1000L / sampleRate) + 20)
            audioTrack.release()
        } catch (_: Exception) {
            // Ignore audio generation failures gracefully
        }
    }
}
