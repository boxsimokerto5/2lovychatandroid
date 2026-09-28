package com.example.util

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

/**
 * Perekam Pesan Suara (Voice Note) ringan menggunakan format AAC (.m4a).
 * Menggunakan MediaRecorder bawaan Android dengan bitrate teroptimasi (sangat jernih & ringan, ~8KB/detik).
 */
class VoiceRecorder(private val context: Context) {

    private var mediaRecorder: MediaRecorder? = null
    private var currentOutputFile: File? = null
    private var recordingStartTimeMs: Long = 0L

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _recordingDurationSeconds = MutableStateFlow(0)
    val recordingDurationSeconds: StateFlow<Int> = _recordingDurationSeconds.asStateFlow()

    private val _amplitudeFlow = MutableStateFlow(0f)
    val amplitudeFlow: StateFlow<Float> = _amplitudeFlow.asStateFlow()

    private var tickerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    fun startRecording(): File? {
        if (_isRecording.value) {
            cancelRecording()
        }

        val cacheDir = File(context.cacheDir, "voice_notes")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }

        val file = File(cacheDir, "vn_${System.currentTimeMillis()}.m4a")
        currentOutputFile = file

        val recorder = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Gagal membuat MediaRecorder", e)
            return null
        }

        return try {
            recorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(64000) // 64 kbps (kualitas vokal sangat jernih dan hemat data)
                setAudioSamplingRate(44100)
                setOutputFile(file.absolutePath)
                prepare()
                start()
            }

            mediaRecorder = recorder
            recordingStartTimeMs = System.currentTimeMillis()
            _isRecording.value = true
            _recordingDurationSeconds.value = 0
            _amplitudeFlow.value = 0f

            startTicker()
            Log.d(TAG, "Perekaman suara dimulai: ${file.name}")
            file
        } catch (e: IOException) {
            Log.e(TAG, "Gagal memulai rekaman suara (prepare/start)", e)
            releaseRecorder()
            file.delete()
            null
        } catch (e: Exception) {
            Log.e(TAG, "Kesalahan tidak terduga saat merekam suara", e)
            releaseRecorder()
            file.delete()
            null
        }
    }

    /**
     * Menghentikan perekaman dan mengembalikan pasangan File rekaman & durasi (detik).
     * Jika rekaman terlalu singkat (< 1 detik), rekaman otomatis dibatalkan.
     */
    fun stopRecording(): Pair<File, Int>? {
        if (!_isRecording.value || mediaRecorder == null) {
            return null
        }

        val file = currentOutputFile
        val durationMs = System.currentTimeMillis() - recordingStartTimeMs
        val durationSec = (durationMs / 1000).toInt().coerceAtLeast(1)

        try {
            mediaRecorder?.stop()
            Log.d(TAG, "Perekaman suara selesai: ${file?.name} ($durationSec detik)")
        } catch (e: Exception) {
            Log.w(TAG, "Stop MediaRecorder error (mungkin durasi terlalu pendek): ${e.message}")
        } finally {
            releaseRecorder()
        }

        if (durationMs < 800L || file == null || !file.exists() || file.length() == 0L) {
            Log.d(TAG, "Rekaman suara diabaikan karena durasi terlalu singkat (<800ms)")
            file?.delete()
            return null
        }

        return Pair(file, durationSec)
    }

    /**
     * Membatalkan perekaman dan menghapus file sementara.
     */
    fun cancelRecording() {
        try {
            mediaRecorder?.stop()
        } catch (_: Exception) {}
        releaseRecorder()
        currentOutputFile?.delete()
        currentOutputFile = null
        Log.d(TAG, "Perekaman suara dibatalkan oleh pengguna.")
    }

    private fun releaseRecorder() {
        tickerJob?.cancel()
        tickerJob = null
        try {
            mediaRecorder?.reset()
            mediaRecorder?.release()
        } catch (_: Exception) {}
        mediaRecorder = null
        _isRecording.value = false
        _amplitudeFlow.value = 0f
    }

    private fun startTicker() {
        tickerJob?.cancel()
        tickerJob = scope.launch {
            while (isActive && _isRecording.value) {
                delay(100L)
                val durationSec = ((System.currentTimeMillis() - recordingStartTimeMs) / 1000).toInt()
                _recordingDurationSeconds.value = durationSec

                // Ambil amplitude untuk animasi gelombang suara (0..1f)
                val amp = try {
                    val maxAmp = mediaRecorder?.maxAmplitude ?: 0
                    (maxAmp / 32767f).coerceIn(0f, 1f)
                } catch (_: Exception) {
                    0f
                }
                _amplitudeFlow.value = amp

                // Batas maksimal rekaman adalah 60 detik
                if (durationSec >= 60) {
                    break
                }
            }
        }
    }

    companion object {
        private const val TAG = "VoiceRecorder"
    }
}
