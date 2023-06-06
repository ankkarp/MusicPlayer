package com.example.musicplayer.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "playlist_music")
data class PlaylistMusic(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val playlistId: Long,
    val musicId: Long
)

