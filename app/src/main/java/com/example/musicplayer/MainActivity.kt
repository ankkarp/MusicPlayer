package com.example.musicplayer

//import com.google.accompanist.permissions.PermissionRequired
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicplayer.view.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerControlView

class MainActivity : ComponentActivity() {
//    private val permissionsRequired = arrayOf(
//        Manifest.permission.READ_EXTERNAL_STORAGE,
//    )
//    private val askPermissions = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            PermissionsComposeTheme {
            MainContent()
//            }
        }
    }
}

@Composable
fun MainContent() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Audio Player", color = Color.White) },
                backgroundColor = BackgroundColor,
            )
        },
        backgroundColor = BackgroundColor,
        content = { MyContent() },
        bottomBar = {
            PlayerControlView(context)
        }
    )
}

fun <T> Cursor.map(f: (Cursor) -> T): List<T> {
    val items = mutableListOf<T>()
    use {
        while (!it.isClosed && it.moveToNext()) {
            items += f(it)
        }
    }
    return items.toList()
}
//@Composable
//fun AppBar(){
//            Text(
//                "b",
//                color = TextDimmerColor,
//                overflow = TextOverflow.Ellipsis,
//                maxLines = 1
//            )
//        DrawableIconButton(
//            icon = R.drawable.ic_addtoqueue,
//            iconSize = 32.dp,
//            iconColor = AccentColor1
//        ) {}
//        DrawableIconButton(
//            icon = R.drawable.ic_more,
//            iconSize = 32.dp,
//            iconColor = AccentColor1
//        ) {}
//}

@Composable
fun musicPlaylistItem(musicItem: Music) {
    Row(modifier = Modifier.fillMaxWidth().background(LighterColor)) {
        DrawableIconButton(
            icon = R.drawable.ic_play,
            iconSize = 32.dp,
            iconColor = AccentColor1,
            onclick = {}
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
            iconSize = 32.dp,
            iconColor = AccentColor1,
            onclick = {}
        )
        DrawableIconButton(
            icon = R.drawable.ic_more,
            iconSize = 32.dp,
            iconColor = AccentColor1,
            onclick = {}
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyContent() {
    val mainContext = LocalContext.current
    val fsPermissionState = rememberPermissionState(
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )
    if (fsPermissionState.hasPermission) {

        val musicFiles = remember {
            val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
            val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID
            )
            val cursor = mainContext.contentResolver.query(
                musicUri,
                projection,
                selection,
                null,
                sortOrder
            )
            cursor?.use {
                it.map { cursor ->
                    Music(
                        id = cursor.getLong(0),
                        title = cursor.getString(1),
                        artist = cursor.getString(2),
                        albumId = cursor.getLong(3)
                    )
                }.toList()
            } ?: emptyList()
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(1.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(musicFiles) { music ->
                musicPlaylistItem(music)
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
                Text("Нет доступа к файловой системе.")
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainContent()
}