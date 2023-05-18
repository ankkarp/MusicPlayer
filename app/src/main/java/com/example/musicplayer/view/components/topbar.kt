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
