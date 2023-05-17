package com.example.musicplayer.view

//import com.google.accompanist.permissions.PermissionRequired
import android.content.Context
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
import androidx.compose.material.SliderDefaults.colors
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicplayer.R
import com.example.musicplayer.model.data.MusicItem
import com.example.musicplayer.model.database.MusicDatabase
import com.example.musicplayer.model.repository.MusicRepository
import com.example.musicplayer.view.*
import com.example.musicplayer.viewmodel.MusicViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainContent()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainContent() {
    val context = LocalContext.current
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

    Scaffold(
        topBar = {
            AppBar(viewModel, fsPermissionState.hasPermission)
        },
        backgroundColor = BackgroundColor,
        content = {
            MyContent(fsPermissionState = fsPermissionState,
                onItemClick = { musicItem: MusicItem ->
                    if (activeMusicItem != null) {
                        viewModel.setActive(activeMusicItem!!, false)
                    }
                    viewModel.setActive(musicItem, true)
                }, musicList = musicList
            )
        },
        bottomBar = {
            PlayerBottomBar(
                hasPermission = fsPermissionState.hasPermission,
                activeMusicItem = activeMusicItem
            )
        }
    )
}

@Composable
fun PlayerSlider() {
    var sliderPosition by remember { mutableStateOf(0f) }
    Slider(
        value = sliderPosition, onValueChange = { sliderPosition = it },
        colors = colors(
            thumbColor = playerButtonsColor, disabledThumbColor = playerButtonsColor,
            activeTrackColor = SliderColor, inactiveTrackColor = SliderColor
        )
    )
}

@Composable
fun PlayerBottomBar(hasPermission: Boolean, activeMusicItem: MusicItem?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DimmerAccentColor1)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "%s - %s".format(activeMusicItem?.artist ?: "...", activeMusicItem?.title ?: "..."),
            modifier = Modifier.fillMaxWidth(),
            color = TextColor,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
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
                onClick = {}
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
                enabled = hasPermission,
                onClick = {}
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

fun <T> Cursor.map(f: (Cursor) -> T): List<T> {
    val items = mutableListOf<T>()
    use {
        while (!it.isClosed && it.moveToNext()) {
            items += f(it)
        }
    }
    return items.toList()
}

@Composable
fun AppBar(viewModel: MusicViewModel, hasPermission: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val mainContext = LocalContext.current

        PlayListSelect()
        Row {
            DrawableIconButton(
                icon = R.drawable.ic_locate,
                iconSize = 32.dp,
                iconColor = AccentColor2,
                enabled = hasPermission,
                onClick = {}
            )
            DrawableIconButton(
                icon = R.drawable.ic_refresh,
                iconSize = 32.dp,
                iconColor = AccentColor2,
                enabled = hasPermission,
                onClick = { loadMusic(context = mainContext, musicViewModel = viewModel) },
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayListSelect() {
    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            readOnly = true,
            value = selectedOptionText,
            onValueChange = { },
            label = { Text("Label") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                textColor = TextColor,
                trailingIconColor = TextColor, focusedLabelColor = TextDimmerColor,
                unfocusedLabelColor = TextDimmerColor,
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}

@Composable
fun musicPlaylistItem(musicItem: MusicItem, onItemClick: (MusicItem) -> Unit) {
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
            onClick = { onItemClick(musicItem) }
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

fun loadMusic(context: Context, musicViewModel: MusicViewModel) {
    val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
    val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM_ID
    )
    val cursor = context.contentResolver.query(
        musicUri,
        projection,
        selection,
        null,
        sortOrder
    )
    val musicFiles = cursor?.use {
        it.map { cursor ->
            MusicItem(
                id = cursor.getLong(0),
                title = cursor.getString(1),
                artist = cursor.getString(2),
                albumId = cursor.getLong(3)
            )
        }.toList()
    } ?: emptyList()
    musicViewModel.insertAll(musicFiles)
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyContent(
    fsPermissionState: PermissionState, onItemClick: (MusicItem) -> Unit,
    musicList: List<MusicItem>
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
                    musicPlaylistItem(music, onItemClick = onItemClick)
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
//                Column {
                Text("Добавьте музыку.")
//                    Button(
//                        onClick = { fsPermissionState.launchPermissionRequest() },
//                        modifier = Modifier.width(200.dp)
//                    ) {
//                        Text("Предоставить доступ к памяти устройства")
//                    }
//                }
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainContent()
}