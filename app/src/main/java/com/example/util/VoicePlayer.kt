package com.example.util

import android.media.AudioAttributes
import android.media.MediaPlayer
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

/**
 * Pemutar Pesan Suara (Voice Note Player) Singleton.
 * Memastikan hanya 1 pesan suara yang berputar dalam satu waktu.
 */
object VoicePlayer {
    private const val TAG = "VoicePlayer"

    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    private val _currentPlayingId = MutableStateFlow<String?>(null)
    val currentPlayingId: StateFlow<String?> = _currentPlayingId.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playbackProgress = MutableStateFlow(0f)
    val playbackProgress: StateFlow<Float> = _playbackProgress.asStateFlow()

    private val _currentPositionSeconds = MutableStateFlow(0)
    val currentPositionSeconds: StateFlow<Int> = _currentPositionSeconds.asStateFlow()

    fun play(messageId: String, audioUrl: String) {
        if (_currentPlayingId.value == messageId) {
            // Jika pesan yang sama sedang di-pause, lanjutkan
            if (mediaPlayer != null && !_isPlaying.value) {
                try {
                    mediaPlayer?.start()
                    _isPlaying.value = true
                    startProgressTracker()
                    return
                } catch (e: Exception) {
                    Log.w(TAG, "Gagal resume audio: ${e.message}")
                }
            } else if (_isPlaying.value) {
                pause()
                return
            }
        }

        // Hentikan pemutaran audio sebelumnya
        stop()

        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(audioUrl)
                setOnPreparedListener { mp ->
                    try {
                        mp.start()
                        _currentPlayingId.value = messageId
                        _isPlaying.value = true
                        startProgressTracker()
                    } catch (e: Exception) {
                        Log.e(TAG, "Error starting MediaPlayer onPrepared", e)
                        stop()
                    }
                }
                setOnCompletionListener {
                    stop()
                }
                setOnErrorListener { _, what, extra ->
                    Log.w(TAG, "MediaPlayer error: what=$what, extra=$extra")
                    stop()
                    true
                }
                prepareAsync()
            }
            mediaPlayer = player
            _currentPlayingId.value = messageId
            _isPlaying.value = true
        } catch (e: Exception) {
            Log.e(TAG, "Gagal memutar audio voice note: ${e.message}", e)
            stop()
        }
    }

    fun pause() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Pause error: ${e.message}")
        }
        _isPlaying.value = false
        progressJob?.cancel()
    }

    fun seekTo(progress: Float) {
        val player = mediaPlayer ?: return
        try {
            val duration = player.duration
            if (duration > 0) {
                val targetMs = (duration * progress.coerceIn(0f, 1f)).toInt()
                player.seekTo(targetMs)
                _playbackProgress.value = progress
                _currentPositionSeconds.value = targetMs / 1000
            }
        } catch (e: Exception) {
            Log.w(TAG, "Seek error: ${e.message}")
        }
    }

    fun stop() {
        progressJob?.cancel()
        progressJob = null
        try {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null
        _currentPlayingId.value = null
        _isPlaying.value = false
        _playbackProgress.value = 0f
        _currentPositionSeconds.value = 0
    }

    private fun startProgressTracker() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isActive && _isPlaying.value) {
                val player = mediaPlayer
                if (player != null && player.isPlaying) {
                    val pos = player.currentPosition
                    val dur = player.duration
                    if (dur > 0) {
                        _playbackProgress.value = (pos.toFloat() / dur.toFloat()).coerceIn(0f, 1f)
                        _currentPositionSeconds.value = pos / 1000
                    }
                }
                delay(80L)
            }
        }
    }
}
