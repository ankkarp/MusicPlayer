package com.example.musicplayer.view

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable
fun DrawableIconButton(icon: Int, iconSize: Dp, iconColor: Color,
                       onClick: () -> Unit, enabled : Boolean = true){
    IconButton(onClick = { onClick() }, enabled=enabled) {
        Icon(
            painter = painterResource(icon),
            contentDescription = "",
            modifier = Modifier.size(iconSize),
            tint = iconColor
        )
    }
}