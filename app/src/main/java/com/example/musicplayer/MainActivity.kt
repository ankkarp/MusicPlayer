package com.example.musicplayer

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainContent() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Audio Player", color = Color.White) },
                    backgroundColor = Color(0xffFF7314)
                )
            },
            content = { MyContent() }
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
        LazyColumn {
            items(musicFiles) { music ->
                Text(music.title)
            }
        }
    }
    else {
        Text("Нет доступа к файловой системе.")
        Button(onClick = { fsPermissionState.launchPermissionRequest() }) {
            Text("Предоставить доступ к памяти устройства")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainContent()
}