package com.example.musicplayer.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music")
data class MusicItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val artist: String,
    val albumId: Long,
    val playlistId: Long = 1,
    val isActive: Boolean = false,
)