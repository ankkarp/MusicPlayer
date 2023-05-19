package com.example.musicplayer.view.audioplayer

import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.compositionLocalOf
import com.example.musicplayer.model.data.MusicItem
import java.io.IOException
import kotlin.properties.Delegates


val LocalAudioPlayer = compositionLocalOf { AudioPlayer() }

class AudioPlayer() {
    lateinit private var audioFilePath: String
    private val mediaPlayer = MediaPlayer()
    var activeMusicItem: MusicItem? = null

//    var playerUIState = PlayerUIState()
    var playerState: PlayerState by Delegates.observable(PlayerState.start(StartMode.standby)) { _, _, playerState ->
//        playerUIState.propagate(playerState)
    }

    private fun preparePlayer() {
        mediaPlayer.setOnCompletionListener {
            playerState = playerState.transition(TransEvent.stopTapped)
        }

//        val os: OutputStream = try { FileOutputStream(audioFilePath) } catch (e: IOException) {
//            Log.e("preparePlayer: ", e.localizedMessage ?: "IOException")
//            return
//        }

        Log.d("setup player:", activeMusicItem?.uri.toString())
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            mediaPlayer.setDataSource(activeMusicItem!!.uri)
            mediaPlayer.setVolume(1.0f, 1.0f) // 0.0 to 1.0 raw scalar
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener { mediaPlayer.start() }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun setupPlayer(musicItem: MusicItem) {
        this.activeMusicItem = musicItem
        mediaPlayer.reset()
        playerState = PlayerState.start(StartMode.play)
//        audio = Base64.decode(audioStr, Base64.DEFAULT)
        preparePlayer()
    }

    fun playTapped() {
        with (mediaPlayer) {
            if (playerState is PlayerState.playing) {
                pause()
            } else {
                this.start()
            }
        }
        playerState = playerState.transition(TransEvent.playTapped)
    }

    fun stopTapped() {
        mediaPlayer.pause()
        mediaPlayer.seekTo(0)
        playerState = playerState.transition(TransEvent.stopTapped)
    }

    fun ffwdTapped() {
        mediaPlayer.seekTo(mediaPlayer.currentPosition+10000)
    }

    fun rwndTapped() {
        mediaPlayer.seekTo(mediaPlayer.currentPosition-10000)
    }
}