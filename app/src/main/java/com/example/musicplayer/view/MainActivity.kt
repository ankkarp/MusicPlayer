package com.example.musicplayer.view

//import com.google.accompanist.permissions.PermissionRequired
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
//    if (activeMusicItem == null){
//        viewModel.setActive(musicList[0], true)
//    }
    val context = LocalContext.current
    val player = ExoPlayer.Builder(context).build()
    var activeMusicItemIdx by remember { mutableStateOf(-1) }
    if (musicList.isNotEmpty()) {
        println(musicList[0])
        println(musicList.map{it.uri})
        musicList.forEach { player.addMediaItem(MediaItem.fromUri(it.uri)) }
    }
    player.prepare()
    var playlistNames by remember { mutableStateOf(listOf("По умолчанию", "Очередь")) }
    var selectedPlaylistName by remember { mutableStateOf(playlistNames[0]) }
//    var queueMusicItems = ArrayList<MusicItem>()

    Scaffold(
        topBar = {
            AppBar(viewModel, fsPermissionState.hasPermission, playlistNames, selectedPlaylistName,
            setPlaylist = { selectedOption: String ->
                    selectedPlaylistName = selectedOption
            })
        },
        backgroundColor = BackgroundColor,
        content = {
            MyContent(
                fsPermissionState = fsPermissionState,
                onItemClick = { musicItem: MusicItem ->
                    activeMusicItemIdx = musicList.indexOf(musicItem)
                    println(activeMusicItemIdx)
                    player.playWhenReady = false
                    player.seekTo(activeMusicItemIdx, 0)
                    viewModel.setActive(musicItem, true)
                    println("count: ${player.mediaItemCount}")
                    if (activeMusicItem != null) {
                        viewModel.setActive(activeMusicItem!!, false)
                    }
                    player.playWhenReady = true
                }, musicList = musicList, activeMusicItem = activeMusicItem,
//                queueMusicItems = queueMusicItems,
//                addToQueue = {musicItem : MusicItem -> queueMusicItems.add(musicItem)}
            )
        },
        bottomBar = {
            PlayerBottomBar(
                hasPermission = fsPermissionState.hasPermission,
                activeMusicItem = activeMusicItem,
//                player = player,
                play = {
                    player.playWhenReady = !player.isPlaying
                },
                seekToNext = {
                        if (activeMusicItem != null && activeMusicItemIdx < musicList.count() - 1) {
                            viewModel.setActive(activeMusicItem!!, false)
                            activeMusicItemIdx += 1
                            player.seekTo(activeMusicItemIdx, 0)
                            viewModel.setActive(musicList[activeMusicItemIdx], true)
                        }
                    },
                seekToPrev = {
                        if (activeMusicItem != null && activeMusicItemIdx > 0) {
                            viewModel.setActive(activeMusicItem!!, false)
                            activeMusicItemIdx -= 1
                            player.seekTo(activeMusicItemIdx, 0)
                            viewModel.setActive(musicList[activeMusicItemIdx], true)
                        }
                    },
                player
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainContent()
}