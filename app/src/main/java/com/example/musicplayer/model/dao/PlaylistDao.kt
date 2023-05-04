package com.example.musicplayer.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicplayer.model.data.MusicItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM music ORDER BY id ASC")
    fun getAll(): Flow<MusicItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(musicItem: MusicItem)
}