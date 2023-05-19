package com.example.musicplayer.view.audioplayer
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.ui.graphics.Color
//import com.example.musicplayer.R
//
//class PlayerUIState {
//    private var recVisible = true
//    var recEnabled by mutableStateOf(true)
//    var recColor by mutableStateOf(Color.Black)
//    var recIcon by mutableStateOf(R.drawable.ic_baseline_radio_button_checked_24) // initial value
//
//    var playCtlEnabled by mutableStateOf(false)
//    var playCtlColor by mutableStateOf(Color.LightGray)
//
//    var playEnabled by mutableStateOf(false)
//    var playColor by mutableStateOf(Color.LightGray)
//    var playIcon by mutableStateOf(R.drawable.ic_baseline_play_arrow_24) // initial value
//
//    var doneEnabled by mutableStateOf(true)
//    var doneColor by mutableStateOf(Color.DarkGray)
//    var doneIcon by mutableStateOf(R.drawable.ic_baseline_share_24) // initial value
//
//    private fun reset() {
//
//        playCtlEnabled = false
//        playCtlColor = Color.LightGray
//
//        playEnabled = false
//        playColor = Color.LightGray
//        playIcon = R.drawable.ic_baseline_play_arrow_24 // initial value
//
//        doneEnabled = true
//        doneColor = Color.DarkGray
//        doneIcon = R.drawable.ic_baseline_share_24 // initial value
//    }
//
//    private fun playCtlEnabled(enabled: Boolean) {
//        playCtlEnabled = enabled
//        playCtlColor = if (enabled) Color.DarkGray else Color.LightGray
//    }
//
//    private fun playEnabled(enabled: Boolean) {
//        playIcon = R.drawable.ic_baseline_play_arrow_24
//        playEnabled = enabled
//        playColor = if (enabled) Color.DarkGray else Color.LightGray
//    }
//
//    private fun pauseEnabled(enabled: Boolean) {
//        playIcon = R.drawable.ic_baseline_pause_24
//        playEnabled = enabled
//        playColor = if (enabled) Color.DarkGray else Color.LightGray
//    }
//
//    private fun doneEnabled(enabled: Boolean) {
//        doneEnabled = enabled
//        doneColor = if (enabled) Color.DarkGray else Color.LightGray
//    }
//
//    fun propagate(playerState: PlayerState) = when (playerState) {
//        is PlayerState.start -> {
//            when (playerState.mode) {
//                StartMode.play -> {
//                    recVisible = false
//                    recEnabled = false
//                    recColor = Color.Transparent
//                    playEnabled(true)
//                    playCtlEnabled(false)
//                    doneIcon = R.drawable.ic_baseline_exit_to_app_24
//                    doneColor = Color.DarkGray
//                }
//                StartMode.standby -> {
//                    playEnabled(true)
//                    playCtlEnabled(false)
//                    doneEnabled(true)
//                }
//            }
//        }
//        is PlayerState.paused -> {
//            playIcon = R.drawable.ic_baseline_play_arrow_24
//        }
//        is PlayerState.playing -> {
//            pauseEnabled(true)
//            playCtlEnabled(true)
//        }
//    }
//}