package com.example.musicplayer.model.dao

import androidx.room.*
import com.example.musicplayer.model.data.MusicItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {
    @Query("SELECT * FROM music")
    fun getAll(): Flow<List<MusicItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(musicItem: MusicItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(songs: List<MusicItem>)

    @Update
    suspend fun update(musicItem: MusicItem)

    @Delete
    suspend fun delete(musicItem: MusicItem)
}