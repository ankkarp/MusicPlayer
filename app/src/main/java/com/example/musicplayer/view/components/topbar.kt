package com.example.musicplayer.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.musicplayer.R
import com.example.musicplayer.view.*
import com.example.musicplayer.viewmodel.MusicViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayListSelect(playlistNames: List<String>, selectedPlaylist: String,
                   setPlaylist: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            readOnly = true,
            value = selectedPlaylist,
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
            playlistNames.forEach { selectedOption ->
                DropdownMenuItem(
                    onClick = {
                        setPlaylist(selectedOption)
                        expanded = false
                    }
                ) {
                    Text(text = selectedOption)
                }
            }
        }
    }
}


@Composable
fun AppBar(viewModel: MusicViewModel, hasPermission: Boolean, playlistNames: List<String>,
           selectedPlaylist: String, setPlaylist: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val mainContext = LocalContext.current

        PlayListSelect(playlistNames, selectedPlaylist, setPlaylist)
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
