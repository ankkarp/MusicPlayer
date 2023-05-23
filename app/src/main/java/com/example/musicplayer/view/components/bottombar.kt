package com.example.musicplayer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayer.R
import com.example.musicplayer.model.data.MusicItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun PlayerSlider(player: ExoPlayer) {
    var sliderPosition by remember { mutableStateOf(0f) }
    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying_: Boolean) {
                isPlaying = isPlaying_
            }
        }
        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
        }
    }
    if (isPlaying) {
        LaunchedEffect(Unit) {
            while(true) {
                sliderPosition = player.currentPosition.toFloat()
                delay(1.seconds / 30)
            }
        }
    }
    if (player.contentDuration > 0){
        Box {
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                valueRange = 0f..player.contentDuration.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = playerButtonsColor, disabledThumbColor = playerButtonsColor,
                    activeTrackColor = SliderColor, inactiveTrackColor = SliderColor
                ),
            )
        }
    }
}

//@Composable
//fun PlayerSlider(player: ExoPlayer) {
//    var currentValue by remember { mutableStateOf(0L) }
//    var isPlaying by remember { mutableStateOf(false) }
//
//    DisposableEffect(Unit) {
//        val listener = object : Player.Listener {
//            override fun onIsPlayingChanged(isPlaying_: Boolean) {
//                isPlaying = isPlaying_
//            }
//        }
//        player.addListener(listener)
//        onDispose {
//            player.removeListener(listener)
//        }
//    }
//    if (isPlaying) {
//        LaunchedEffect(Unit) {
//            while(true) {
//                currentValue = player.currentPosition
//                delay(1.seconds / 30)
//            }
//        }
//    }

//    Box {
//        Slider(
//            modifier = Modifier.weight(1f),
//            value = currentValue ,
//            onValueChange = {currentValue = it },
//            valueRange = 0f.. mediaPlayer.contentDuration()
//        )
//        Slider(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(4.dp),
//            value = currentValue.toFloat(),
//            onValueChange = { value ->
//                currentValue = value.toInt()
//            },
//            onValueChangeFinished = {
//                isUserDraggingSlider = false
//                player.seekTo(currentPosition.value.toLong())
//            },
//            colors = SliderDefaults.colors(
//                thumbColor = playerButtonsColor, disabledThumbColor = playerButtonsColor,
//                activeTrackColor = SliderColor, inactiveTrackColor = SliderColor
//            ),
//        )
//    }
//
//    val scope = rememberCoroutineScope()
//
//    DisposableEffect(currentPosition) {
//        val playbackPosition = object : Player.Listener {
//            override fun onPlaybackStateChanged(state: Int) {
//                if (state == Player.STATE_READY && player.playWhenReady) {
//                    scope.launch {
//                        while (currentPosition.value < duration.toInt() && !isUserDraggingSlider) {
//                            currentPosition.value = player.currentPosition.toInt()
//                            delay(16)
//                        }
//                    }
//                }
//            }
//
//            override fun onIsPlayingChanged(isPlaying: Boolean) {
//                if (isPlaying) {
//                    scope.launch {
//                        while (currentPosition.value < duration.toInt() && !isUserDraggingSlider) {
//                            currentPosition.value = player.currentPosition.toInt()
//                            delay(16)
//                        }
//                    }
//                }
//            }
//        }
//
//        player.addListener(playbackPosition)
//
//        onDispose {
//            player.removeListener(playbackPosition)
//        }
//    }
//}
//
//@Composable
//fun PlayerSlider(seekToTime: (Long) -> Unit) {
//    var sliderPosition by remember { mutableStateOf(0f) }
//    var isUserDraggingSlider by remember { mutableStateOf(false) }
//    Slider(
//        value = sliderPosition, onValueChange = { sliderPosition = it },
//        colors = SliderDefaults.colors(
//            thumbColor = playerButtonsColor, disabledThumbColor = playerButtonsColor,
//            activeTrackColor = SliderColor, inactiveTrackColor = SliderColor
//        ),
//        onValueChange = { value: FLoat ->
//            sliderPosition = value
//        },
//        onValueChangeFinished = {
//            isUserDraggingSlider = false
//            player.seekTo(currentPosition.value.toLong())
//        },
//    )
//}

@Composable
fun PlayerBottomBar(
    hasPermission: Boolean, activeMusicItem: MusicItem?,
    play: () -> Unit, seekToNext: () -> Unit, seekToPrev: () -> Unit,
    player: ExoPlayer, isPlaying: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DimmerAccentColor1)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "%s - %s".format(activeMusicItem?.artist ?: "<unknown>",
                activeMusicItem?.title ?: "<unknown>"),
            modifier = Modifier.fillMaxWidth(),
            color = TextColor,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DrawableIconButton(
                icon = R.drawable.ic_addtoplaylist,
                iconSize = 32.dp,
                iconColor = playerButtonsColor,
                enabled = hasPermission,
                onClick = {
//                    if (player == null) {
//                    player = MediaPlayer.create(context, R.raw.yourSound);
//                }
//                    mediaPlayer?.start()}
                }
            )
            DrawableIconButton(
                icon = R.drawable.ic_favourite,
                iconSize = 32.dp,
                iconColor = playerButtonsColor,
                enabled = hasPermission,
                onClick = {},
            )
            DrawableIconButton(
                icon = R.drawable.ic_playprevious,
                iconSize = 32.dp,
                iconColor = playerButtonsColor,
                enabled = hasPermission,
                onClick = { seekToPrev() }
            )
            DrawableIconButton(
                icon = if (isPlaying) R.drawable.ic_play else R.drawable.ic_pause,
                iconSize = 32.dp,
                iconColor = playerButtonsColor,
                enabled = hasPermission && (activeMusicItem != null),
                onClick = {play()}
            )
            DrawableIconButton(
                icon = R.drawable.ic_playnext,
                iconSize = 32.dp,
                iconColor = playerButtonsColor,
                enabled = hasPermission,
                onClick = { seekToNext() }
            )
            DrawableIconButton(
                icon = R.drawable.ic_shuffle,
                iconSize = 32.dp,
                iconColor = playerButtonsColor,
                enabled = hasPermission,
                onClick = {}
            )
            DrawableIconButton(
                icon = R.drawable.ic_repeat,
                iconSize = 32.dp,
                iconColor = playerButtonsColor,
                enabled = hasPermission,
                onClick = {}
            )
        }
        PlayerSlider(player)
    }
}