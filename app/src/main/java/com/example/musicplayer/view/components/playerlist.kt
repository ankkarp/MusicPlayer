package com.example.musicplayer.view.components

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.musicplayer.R
import com.example.musicplayer.model.data.MusicItem
import com.example.musicplayer.view.*
//import com.example.musicplayer.view.audioplayer.LocalAudioPlayer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState


@Composable
fun musicPlaylistItem(musicItem: MusicItem, onItemClick: (MusicItem) -> Unit,
                      activeMusicItem: MusicItem?) {
//    val audioPlayer = LocalAudioPlayer.current
    val context = LocalContext.current
//    val mediaPlayer = when (activeMusicItem == null){
//        true -> null
//        false -> MediaPlayer.create(context, Uri.parse(activeMusicItem.uri))
//    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(49.dp)
            .background(LighterColor),
        verticalAlignment = Alignment.Top
    ) {
        DrawableIconButton(
            icon = R.drawable.ic_play,
            iconSize = 29.dp,
            iconColor = AccentColor1,
            onClick = {
                onItemClick(musicItem)
//                val mMediaPlayer = MediaPlayer().apply {
//                    setDataSource(musicItem.uri)
//                    setAudioAttributes(AudioAttributes.Builder()
//                        .setUsage(AudioAttributes.USAGE_MEDIA)
//                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                        .build()
//                    )
//                    prepareAsync()
//                    start()
//                if (audioPlayer.activeMusicItem != musicItem){
//                    audioPlayer.setupPlayer(musicItem)
//                }
//                audioPlayer.playTapped()
//                player = MediaPlayer.create(context, Uri.parse(musicItem.uri))
//                if (mediaPlayer != null){
//                    println("aaaaaaaaa")
//                    mediaPlayer.start()
//                }
            }
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                musicItem.title,
                color = TextDimmerColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                musicItem.artist,
                color = TextDimmerColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        DrawableIconButton(
            icon = R.drawable.ic_addtoqueue,
            iconSize = 29.dp,
            iconColor = AccentColor1,
            onClick = {}
        )
        DrawableIconButton(
            icon = R.drawable.ic_more,
            iconSize = 29.dp,
            iconColor = AccentColor1,
            onClick = {}
        )
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyContent(
    fsPermissionState: PermissionState, onItemClick: (MusicItem) -> Unit,
    musicList: List<MusicItem>, activeMusicItem: MusicItem?,
//    player: MutableState<MediaPlayer?>
) {
    if (fsPermissionState.hasPermission) {
//        loadMusic(mainContext, viewModel)
        if (musicList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(musicList) { music ->
                    musicPlaylistItem(music, onItemClick, activeMusicItem)
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp),
                contentAlignment = Alignment.Center
            )
            {
                Text("Добавьте музыку.")
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp),
            contentAlignment = Alignment.Center
        )
        {
            Column {
                Button(
                    onClick = { fsPermissionState.launchPermissionRequest() },
                    modifier = Modifier.width(200.dp)
                ) {
                    Text("Предоставить доступ к памяти устройства")
                }
            }
        }
    }
}