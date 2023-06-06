package com.example.musicplayer.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicplayer.model.data.MusicItem
import com.example.musicplayer.model.data.PlaylistInfo
import com.example.musicplayer.model.data.PlaylistMusic
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistMusicDao {
    @Query("SELECT m.* from music m INNER JOIN playlist_music p on m.id = p.musicId WHERE p.playlistId = :playlistId")
    fun getPlaylistMusic(playlistId: Long): Flow<List<MusicItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlistMusic: PlaylistMusic)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(playlistMusic: List<PlaylistMusic>)
}