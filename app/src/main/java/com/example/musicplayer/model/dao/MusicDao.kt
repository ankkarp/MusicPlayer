package com.example.musicplayer.model.dao

import androidx.room.*
import com.example.musicplayer.model.data.MusicItem
import com.example.musicplayer.model.data.PlaylistInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {

    @Query("SELECT * FROM music ORDER BY id DESC")
    fun getAllMusic(): Flow<List<MusicItem>>

    @Query("SELECT * FROM music WHERE isActive = true LIMIT 1")
    fun getActive(): Flow<MusicItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(musicItem: MusicItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(songs: List<MusicItem>)

    @Update
    suspend fun update(musicItem: MusicItem)

    @Update
    suspend fun updateAll(musicItem: List<MusicItem>)

    @Delete
    suspend fun delete(musicItem: MusicItem)
}