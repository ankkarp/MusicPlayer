package com.example.musicplayer.view

//import com.google.accompanist.permissions.PermissionRequired
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayer.model.data.MusicItem
import com.example.musicplayer.model.database.MusicDatabase
import com.example.musicplayer.model.repository.MusicRepository
import com.example.musicplayer.view.*
import com.example.musicplayer.view.audioplayer.AudioPlayer
import com.example.musicplayer.view.components.AppBar
import com.example.musicplayer.view.components.MyContent
import com.example.musicplayer.viewmodel.MusicViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class MainActivity : ComponentActivity() {
//    private lateinit var playerView: StyledPlayerView
//    private lateinit var exoPlayer: ExoPlayer

//    var audioPlayer = AudioPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//        exoPlayer = ExoPlayer.Builder(this).build()


        setContent {
            MainContent()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainContent() {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(
            color = BackgroundColor,
        )

        systemUiController.setNavigationBarColor(
            color = BackgroundColor,
        )
        onDispose {}
    }

    val viewModel = MusicViewModel(
        MusicRepository(
            MusicDatabase.getDatabase(LocalContext.current).musicDao()
        )
    )
    val fsPermissionState = rememberPermissionState(
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val musicList by viewModel.allMusic.observeAsState(emptyList())
    val activeMusicItem by viewModel.activeMusicItem.observeAsState()
    if (musicList.isNotEmpty()) {
        println(musicList[0])
    }
    println(musicList.map{it.uri})
//    val player = mutableStateOf<MediaPlayer?>(null)
//    val mediaSource = extractMediaSourceFromUri(Uri.parse("asset:///heart_attack.mp3"))
//    val exoPlayer = ExoPlayerFactory.newSimpleInstance(
//        baseContext, DefaultRenderersFactory(baseContext)
//        , DefaultTrackSelector(),
//        DefaultLoadControl()
//    )
//    exoPlayer.apply {
//        // AudioAttributes here from exoplayer package !!!
//        val attr = AudioAttributes.Builder().setUsage(C.USAGE_MEDIA)
//            .setContentType(C.CONTENT_TYPE_MUSIC)
//            .build()
//        // In 2.9.X you don't need to manually handle audio focus :D
//        setAudioAttributes(attr, true)
//        prepare(mediaSource)
//        // THAT IS ALL YOU NEED
//        playWhenReady = true
//    }
    val context = LocalContext.current
    val player = ExoPlayer.Builder(context).build()
    musicList.forEach { player.addMediaItem(MediaItem.fromUri(it.uri)) }
    player.prepare()
    player.playWhenReady = true
//    println("count: ${player.mediaItemCount}")
//    println("state: ${player.playbackState == STATE_READY}")
//    player.play()
//    player.play()

//    val player =  MediaPlayer.create(context, R.raw.audio)
//    mediaPlayer.setDataSource(musicList[0].uri)
//    mediaPlayer.prepare()
//    mediaPlayer.start()
//    val player = MediaPlayer()

    Scaffold(
        topBar = {
            AppBar(viewModel, fsPermissionState.hasPermission)
        },
        backgroundColor = BackgroundColor,
        content = {
            MyContent(
                fsPermissionState = fsPermissionState,
                onItemClick = { musicItem: MusicItem ->
                    if (activeMusicItem != null) {
                        viewModel.setActive(activeMusicItem!!, false)
                    }
                    viewModel.setActive(musicItem, true)
                }, musicList = musicList, activeMusicItem = activeMusicItem
            )
        },
        bottomBar = {
            PlayerBottomBar(
                hasPermission = fsPermissionState.hasPermission,
                activeMusicItem = activeMusicItem,
                player = player
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainContent()
}