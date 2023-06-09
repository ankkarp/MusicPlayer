package com.example.musicplayer.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class PlaylistInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val isActive: Boolean,
)