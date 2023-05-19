package com.example.musicplayer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayer.R
import com.example.musicplayer.model.data.MusicItem
import com.example.musicplayer.viewmodel.MusicViewModel


@Composable
fun PlayerSlider() {
    var sliderPosition by remember { mutableStateOf(0f) }
    Slider(
        value = sliderPosition, onValueChange = { sliderPosition = it },
        colors = SliderDefaults.colors(
            thumbColor = playerButtonsColor, disabledThumbColor = playerButtonsColor,
            activeTrackColor = SliderColor, inactiveTrackColor = SliderColor
        )
    )
}

@Composable
fun PlayerBottomBar(hasPermission: Boolean, activeMusicItem: MusicItem?, player: ExoPlayer) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DimmerAccentColor1)
            .padding(horizontal = 20.dp),
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
                onClick = {}
            )
            DrawableIconButton(
                icon = R.drawable.ic_play,
                iconSize = 32.dp,
                iconColor = playerButtonsColor,
                enabled = hasPermission && (activeMusicItem != null),
                onClick = {
                    println(player.isPlaying)
                    player.playWhenReady = !player.isPlaying
                }
            )
            DrawableIconButton(
                icon = R.drawable.ic_playnext,
                iconSize = 32.dp,
                iconColor = playerButtonsColor,
                enabled = hasPermission,
                onClick = {}
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
        PlayerSlider()
    }
}