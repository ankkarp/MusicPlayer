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
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.google.android.exoplayer2.ui.PlayerControlView

class MainActivity : ComponentActivity() {
//    private val permissionsRequired = arrayOf(
//        Manifest.permission.READ_EXTERNAL_STORAGE,
//    )
//    private val askPermissions = arrayListOf<String>()

//    val database by lazy { MusicDatabase.getDatabase(this) }

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
    val context = LocalContext.current
    val systemUiController = rememberSystemUiController()
//    val useDarkIcons = !isSystemInDarkTheme()

    DisposableEffect(systemUiController) {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = BackgroundColor,
//            darkIcons = useDarkIcons
        )

        systemUiController.setNavigationBarColor(
            color = BackgroundColor,
//            darkIcons = useDarkIcons
        )

        // setStatusBarColor() and setNavigationBarColor() also exist

        onDispose {}
    }

    val viewModel = MusicViewModel(MusicRepository(
        MusicDatabase.getDatabase(LocalContext.current).musicDao()))
    val fsPermissionState = rememberPermissionState(
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    Scaffold(
        topBar = {
            AppBar(viewModel, fsPermissionState)
        },
        backgroundColor = BackgroundColor,
        content = { MyContent(viewModel, fsPermissionState) },
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AppBar(viewModel: MusicViewModel, fsPermissionState: PermissionState) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .padding(10.dp),
    horizontalArrangement = Arrangement.SpaceBetween) {
        val mainContext = LocalContext.current

        PlayListSelect()
        Row(){
            DrawableIconButton(
                icon = R.drawable.ic_locate,
                iconSize = 32.dp,
                iconColor = AccentColor2,
                onClick = {}
            )
            DrawableIconButton(
                icon = R.drawable.ic_refresh,
                iconSize = 32.dp,
                iconColor = AccentColor2,
                onClick = {loadMusic(context = mainContext, musicViewModel = viewModel)}
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayListSelect(){
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
            colors = ExposedDropdownMenuDefaults.textFieldColors(textColor = TextColor,
                                                                trailingIconColor = TextColor, focusedLabelColor = TextDimmerColor,
                unfocusedLabelColor = TextDimmerColor, )
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
                ){
                    Text(text = selectionOption)
                }
            }
        }
    }
}

@Composable
fun musicPlaylistItem(musicItem: MusicItem) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(49.dp)
        .background(LighterColor),
        verticalAlignment = Alignment.Top
    ) {
        DrawableIconButton(
            icon = R.drawable.ic_play,
            iconSize = 29.dp,
            iconColor = AccentColor1,
            onClick = {}
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

fun loadMusic(context: Context, musicViewModel: MusicViewModel){
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
fun MyContent(viewModel: MusicViewModel, fsPermissionState: PermissionState) {
    val musicList by viewModel.allMusic.observeAsState(emptyList())
    val mainContext = LocalContext.current
    if (fsPermissionState.hasPermission) {
        loadMusic(mainContext, viewModel)
        if (musicList.isNotEmpty()){
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(musicList) { music ->
                    musicPlaylistItem(music)
                }
            }
        }
        else{
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