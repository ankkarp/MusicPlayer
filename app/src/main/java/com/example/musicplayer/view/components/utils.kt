package com.example.musicplayer.view

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.example.musicplayer.model.data.MusicItem
import com.example.musicplayer.viewmodel.MusicViewModel

fun <T> Cursor.map(f: (Cursor) -> T): List<T> {
    val items = mutableListOf<T>()
    use {
        while (!it.isClosed && it.moveToNext()) {
            items += f(it)
        }
    }
    return items.toList()
}

fun loadMusic(context: Context, musicViewModel: MusicViewModel) {
    val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
    val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
    println('a')
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM_ID,
    )
    println('b')
    val cursor = context.contentResolver.query(
        musicUri,
        projection,
        selection,
        null,
        sortOrder
    )
    println('c')
    val musicFiles = cursor?.use {
        it.map { cursor ->
            MusicItem(
                id = cursor.getLong(0),
                title = cursor.getString(1),
                artist = cursor.getString(2),
                albumId = cursor.getLong(3),
                uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    cursor.getLong(0)
                ).toString()
            )
        }.toList()
    } ?: emptyList()
    musicViewModel.insertAll(musicFiles)
}