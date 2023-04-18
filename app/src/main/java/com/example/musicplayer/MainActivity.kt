package com.example.musicplayer

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Calling the composable function
            // to display element and its contents
            MainContent()
        }
    }
}

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

@Composable
fun MyContent() {

    val mainContext = LocalContext.current

    val mediaPlayer = MediaPlayer.create(mainContext, R.raw.audio)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row {
            IconButton(onClick = { if (mediaPlayer.isPlaying) mediaPlayer.pause() else mediaPlayer.start()}) {
                Icon(
                    painter = painterResource(id = if (mediaPlayer.isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                    contentDescription = "",
                    Modifier.size(32.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainContent()
}