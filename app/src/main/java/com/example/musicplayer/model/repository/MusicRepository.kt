package com.example.musicplayer.model.repository

import com.example.musicplayer.model.dao.MusicDao
import com.example.musicplayer.model.data.MusicItem
import kotlinx.coroutines.flow.Flow

class MusicRepository(private val musicDao: MusicDao) {
    val allMusic: Flow<List<MusicItem>> = musicDao.getAll()

    suspend fun insert(music: MusicItem) {
        musicDao.insert(music)
    }

    suspend fun insertAll(music: List<MusicItem>) {
        musicDao.insertAll(music)
    }

    suspend fun update(music: MusicItem) {
        musicDao.update(music)
    }

    suspend fun delete(music: MusicItem) {
        musicDao.delete(music)
    }
}