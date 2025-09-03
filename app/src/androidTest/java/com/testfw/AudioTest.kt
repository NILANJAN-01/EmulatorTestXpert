package com.testfw


import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class AudioPlaybackTest {

    private lateinit var track: AudioTrack

    @Before
    fun setup() {
        val sampleRate = 16000
        val channelConfig = AudioFormat.CHANNEL_OUT_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat)

        track = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize,
            AudioTrack.MODE_STREAM
        )

        assertEquals(AudioTrack.STATE_INITIALIZED, track.state, "❌ AudioTrack not initialized")
    }

    @Test
    fun testAudioPlaybackStartStop() {
        // Generate a simple 440Hz sine wave for 1 second
        val sampleRate = 16000
        val duration = 1 // sec
        val numSamples = duration * sampleRate
        val buffer = ShortArray(numSamples)

        for (i in buffer.indices) {
            buffer[i] = (Short.MAX_VALUE *
                    kotlin.math.sin(2.0 * Math.PI * 440.0 * i / sampleRate)).toInt().toShort()
        }

        track.play()
        val written = track.write(buffer, 0, buffer.size)
        assertTrue(written > 0, "❌ Failed to write audio data to track")
        track.stop()
    }

    @After
    fun tearDown() {
        track.release()
    }
}
