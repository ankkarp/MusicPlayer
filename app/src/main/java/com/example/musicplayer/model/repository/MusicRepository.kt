package com.example.musicplayer.model.repository

import com.example.musicplayer.model.dao.MusicDao
import com.example.musicplayer.model.data.MusicItem
import kotlinx.coroutines.flow.Flow

class MusicRepository(private val musicDao: MusicDao) {
    val allMusic: Flow<List<MusicItem>> = musicDao.getAllMusic()
    val activeMusicItem : Flow<MusicItem> = musicDao.getActive()

    suspend fun insert(music: MusicItem) {
        musicDao.insert(music)
    }

    suspend fun insertAll(music: List<MusicItem>) {
        musicDao.insertAll(music)
    }

    suspend fun setActive(music: MusicItem, value: Boolean) {
        val updatedMusicItem = music.copy(isActive = value)
        println("${updatedMusicItem.title}: ${updatedMusicItem.isActive}")
        musicDao.update(updatedMusicItem)
    }

    suspend fun update(music: MusicItem) {
        musicDao.update(music)
    }

    suspend fun updateAll(music: List<MusicItem>) {
        musicDao.updateAll(music)
    }

    suspend fun delete(music: MusicItem) {
        musicDao.delete(music)
    }
}