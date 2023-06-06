package com.example.musicplayer.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicplayer.model.data.MusicItem
import com.example.musicplayer.model.data.PlaylistInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Query("SELECT * from playlist")
    fun getAllPlaylists(playlistId: Long): Flow<List<PlaylistInfo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(playlistInfo: PlaylistInfo)
}